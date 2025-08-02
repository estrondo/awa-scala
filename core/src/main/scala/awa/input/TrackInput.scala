package awa.input

import awa.model.data.Client
import awa.model.data.Device
import awa.model.data.StartedAt
import awa.model.data.TraceId

case class TrackInput(
    traceId: TraceId,
    startedAt: StartedAt,
    device: Device,
    client: Client,
)
