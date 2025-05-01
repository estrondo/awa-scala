package awa.model

import awa.model.data.Order
import awa.model.data.Position
import awa.model.data.PositionData
import awa.model.data.StartedAt
import awa.model.data.TagMap
import awa.model.data.TrackPositionId

case class TrackPosition(
    id: TrackPositionId,
    track: Track,
    startedAt: StartedAt,
    position: Position,
    positionData: PositionData,
    tagMap: TagMap,
    order: Option[Order],
)
