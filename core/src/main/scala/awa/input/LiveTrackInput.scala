package awa.input

import awa.model.data.DeviceId
import awa.model.data.DeviceType
import awa.model.data.StartedAt
import awa.model.data.TraceId

case class LiveTrackInput(
    traceId: TraceId,
    startedAt: StartedAt,
    deviceId: DeviceId,
    deviceType: DeviceType,
)
