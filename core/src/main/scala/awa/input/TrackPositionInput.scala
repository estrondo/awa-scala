package awa.input

import awa.model.data.Client
import awa.model.data.Platform
import awa.model.data.Position
import awa.model.data.PositionData
import awa.model.data.StartedAt
import awa.model.data.TagMap
import awa.model.data.TraceId

case class TrackPositionInput(
    traceId: TraceId,
    tagMap: TagMap,
    startedAt: StartedAt,
    platform: Platform,
    client: Client,
    point: Position,
    positionData: PositionData,
)
