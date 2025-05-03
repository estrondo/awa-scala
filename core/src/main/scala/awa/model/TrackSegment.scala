package awa.model

import awa.model.data.CreatedAt
import awa.model.data.Order
import awa.model.data.Segment
import awa.model.data.SegmentData
import awa.model.data.StartedAt
import awa.model.data.TagMap
import awa.model.data.TrackId
import awa.model.data.TrackSegmentId

case class TrackSegment(
    id: TrackSegmentId,
    trackId: TrackId,
    startedAt: StartedAt,
    segment: Segment,
    segmentData: SegmentData,
    tagMap: TagMap,
    createdAt: Option[CreatedAt],
    order: Option[Order],
)
