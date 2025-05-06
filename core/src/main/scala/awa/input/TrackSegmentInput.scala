package awa.input

import awa.model.data.DeviceId
import awa.model.data.DeviceType
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
    deviceId: Option[DeviceId],
    deviceType: Option[DeviceType],
    segment: Segment,
    segmentData: SegmentData,
)
