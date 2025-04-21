package awa.model

import awa.model.data.Order
import awa.model.data.Segment
import awa.model.data.SegmentData
import awa.model.data.StartedAt
import awa.model.data.TagMap
import awa.model.data.TrackSegmentId

case class TrackSegment(
    id: TrackSegmentId,
    track: Track,
    startedAt: StartedAt,
    segment: Segment,
    segmentData: SegmentData,
    tagMap: TagMap,
    order: Option[Order],
)
