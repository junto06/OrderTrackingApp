# OrderTrackingApp

Android app that tracks an order's delivery in real time on a map and lets the
customer chat with the driver, backed by a Kotlin gRPC server.

## Modules

- `app` — the Android application shell, wires up navigation between features.
- `core:navigation` — shared navigation contracts (`Navigator`, destinations).
- `core:rpc` — `ChatApi`/`OrderTrackingApi` interfaces + DTOs; feature modules
  depend on this, not on `proto`.
- `core:common` — gRPC implementations of `core:rpc`, bound via Hilt.
- `feature:base` — shared Compose theme and `BaseFragment`.
- `feature:ordertracking` — map + status UI, subscribes to order tracking updates.
- `feature:chat-api` — public API (navigation destination) for the chat feature.
- `feature:chat` — chat-with-driver UI and data layer.
- `proto` — `.proto` definitions and generated gRPC/protobuf Kotlin code, shared
  by the Android client and the server.
- `server` — standalone Kotlin gRPC server that simulates a driver: emits a
  scripted route/status sequence for order tracking, and auto-replies to chat
  messages.

## Running

1. Start the server:
   ```
   ./gradlew :server:run
   ```
   It listens on port `50051`.

2. The client currently points at a hardcoded host/port (`RpcConfigs.kt` in
   `core:common`) — update `host` to the machine running the server if testing
   on a physical device/emulator that isn't `localhost`.

3. Run the `app` module from Android Studio, or:
   ```
   ./gradlew :app:installDebug
   ```

## Demo data

Order IDs `1001`–`1004` return canned states (pending payment, payment
declined, delivered, not found); any other order ID drives a simulated
10-point route with live status/location updates.
