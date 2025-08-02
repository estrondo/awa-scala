package awa.input

import awa.model.data.Client
import awa.model.data.Device
import awa.model.data.SegmentHorizontalAccuracy
import awa.model.data.SegmentPath
import awa.model.data.SegmentTimestamp
import awa.model.data.SegmentVerticalAccuracy
import awa.model.data.StartedAt
import awa.model.data.TagMap
import awa.model.data.TraceId
import awa.model.data.TrackId

case class TrackSegmentInput(
    trackId: TrackId,
    traceId: TraceId,
    tagMap: TagMap,
    startedAt: StartedAt,
    device: Device,
    client: Client,
    path: SegmentPath,
    timestamp: SegmentTimestamp,
    horizontalAccuracy: SegmentHorizontalAccuracy,
    verticalAccuracy: SegmentVerticalAccuracy,
)
