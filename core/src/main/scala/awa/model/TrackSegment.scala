package awa.model

import awa.model.data.CreatedAt
import awa.model.data.Order
import awa.model.data.SegmentHorizontalAccuracy
import awa.model.data.SegmentPath
import awa.model.data.SegmentTimestamp
import awa.model.data.SegmentVerticalAccuracy
import awa.model.data.StartedAt
import awa.model.data.TagMap
import awa.model.data.TrackId
import awa.model.data.TrackSegmentId

case class TrackSegment(
    id: TrackSegmentId,
    trackId: TrackId,
    startedAt: StartedAt,
    path: SegmentPath,
    timestamp: SegmentTimestamp,
    horizontalAccuracy: SegmentHorizontalAccuracy,
    verticalAccuracy: SegmentVerticalAccuracy,
    tagMap: TagMap,
    createdAt: Option[CreatedAt],
    order: Option[Order],
)
