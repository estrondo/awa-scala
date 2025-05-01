package awa.input

import awa.model.data.DeviceId
import awa.model.data.DeviceType
import awa.model.data.Position
import awa.model.data.PositionData
import awa.model.data.StartedAt
import awa.model.data.TagMap
import awa.model.data.TraceId

case class LiveTrackPositionInput(
    traceId: TraceId,
    tagMap: TagMap,
    startedAt: StartedAt,
    deviceId: Option[DeviceId],
    deviceType: Option[DeviceType],
    point: Position,
    positionData: PositionData,
)
