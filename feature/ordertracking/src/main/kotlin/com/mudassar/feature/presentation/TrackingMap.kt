package com.mudassar.feature.presentation

import android.content.Context
import android.graphics.Point
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.mudassar.feature.domain.TrackingUpdate
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import java.io.File

private const val DefaultZoom = 18.0
private const val MarkerAnimationDurationMillis = 5500
private const val MarkerScreenFractionX = 0.35
private const val MarkerScreenFractionY = 0.7

@Composable
fun TrackingMap(location: TrackingUpdate.Location?, modifier: Modifier = Modifier) {
    // Don't mount the MapView until we have a location to center it on
    if (location == null) {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    MapView(location, modifier)
}

@Composable
private fun MapView(
    location: TrackingUpdate.Location,
    modifier: Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val mapView = remember { createMapView(context) }
    val marker = remember {
        Marker(mapView).apply {
            icon = createBikeIcon(context)
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
        }.also(mapView.overlays::add)
    }
    val route = remember { Polyline().also(mapView.overlays::add) }
    var previousPoint by remember { mutableStateOf<GeoPoint?>(null) }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            mapView.onDetach()
        }
    }

    LaunchedEffect(location) {
        val target = GeoPoint(location.latitude, location.longitude)
        val start = previousPoint

        if (start == null) {
            marker.position = target
            mapView.controller.setZoom(DefaultZoom)
            mapView.controller.setCenter(target)
            mapView.controller.setCenter(offsetCenter(mapView, target))
        } else {
            marker.rotation = start.bearingTo(target).toFloat()
            animate(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = tween(MarkerAnimationDurationMillis, easing = LinearEasing),
            ) { fraction, _ ->
                val interpolated = GeoPoint(
                    start.latitude + (target.latitude - start.latitude) * fraction,
                    start.longitude + (target.longitude - start.longitude) * fraction,
                )
                marker.position = interpolated
                mapView.controller.setCenter(offsetCenter(mapView, interpolated))
                mapView.invalidate()
            }
        }

        route.addPoint(target)
        previousPoint = target
        mapView.invalidate()
    }

    AndroidView(factory = { mapView }, modifier = modifier)
}

private fun offsetCenter(mapView: MapView, target: GeoPoint): GeoPoint {
    val screenPoint = Point()
    mapView.projection.toPixels(target, screenPoint)
    val dx = ((0.5 - MarkerScreenFractionX) * mapView.width).toInt()
    val dy = ((0.5 - MarkerScreenFractionY) * mapView.height).toInt()
    return mapView.projection.fromPixels(screenPoint.x + dx, screenPoint.y + dy) as GeoPoint
}

private fun createMapView(context: Context): MapView {
    Configuration.getInstance().apply {
        userAgentValue = context.packageName
        osmdroidBasePath = context.cacheDir
        osmdroidTileCache = File(context.cacheDir, "tiles")
    }
    return MapView(context).apply {
        setTileSource(TileSourceFactory.MAPNIK)
        setMultiTouchControls(true)
    }
}
