# OrderTrackingApp

Android app that tracks an order's delivery in real time on a map and lets the
customer chat with the driver, backed by a Kotlin gRPC server.

## Demo

https://github.com/user-attachments/assets/4d6a4e66-af5a-4a13-874c-1362fd47c6bd

## Features

- **Order tracking** see an order's status and the driver's live location
  on a map as it updates.
- **Chat** message the driver about an order. Messages are saved locally,
  so chat still works offline and syncs once you're back online. You get a
  notification when the driver replies while you're not on the chat screen.


## Modules

- `app` — the Android application shell, wires up navigation between features.
- `core:navigation` — shared navigation contracts (`Navigator`, destinations).
- `core:base` — small shared building blocks with no Android or gRPC code,
  like `ErrorLogger` and the `@AppScope` qualifier.
- `core:common` — sets up the gRPC channel and other app-wide singletons,
  bound via Hilt.
- `feature:base` — shared Compose theme and `BaseFragment`.
- `feature:ordertracking` — map + status UI, subscribes to order tracking updates.
- `feature:chat-api` — public API (navigation destination, chat tracker) for
  the chat feature, so other modules don't need to depend on `feature:chat`.
- `chat-domain` — the chat feature's repository/model interfaces, with no
  Android or gRPC dependencies.
- `feature:chat` — chat-with-driver UI and data layer: talks to the server
  over gRPC, caches messages in a local Room database so chat still works
  offline, and shows a notification for new driver messages.
- `chat-sync` — a WorkManager job that retries chat messages that failed to
  send while offline.
- `proto` — `.proto` definitions and generated gRPC/protobuf Kotlin code, shared
  by the Android client and the server.
- `server` — standalone Kotlin gRPC server that simulates a driver: emits a
  scripted route/status sequence for order tracking, and lets you type driver
  chat replies from the terminal.

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
