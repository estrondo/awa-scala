package awa.input

import awa.model.data.Client
import awa.model.data.Platform
import awa.model.data.Segment
import awa.model.data.SegmentData
import awa.model.data.StartedAt
import awa.model.data.TagMap
import awa.model.data.TraceId
import awa.model.data.TrackId

case class TrackSegmentInput(
    trackId: TrackId,
    traceId: TraceId,
    tagMap: TagMap,
    startedAt: StartedAt,
    platform: Platform,
    client: Client,
    segment: Segment,
    segmentData: SegmentData,
)
