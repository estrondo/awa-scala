package awa.model

import awa.model.data.AccountId
import awa.model.data.Client
import awa.model.data.CreatedAt
import awa.model.data.Device
import awa.model.data.StartedAt
import awa.model.data.TrackId

case class Track(
    id: TrackId,
    accountId: AccountId,
    startedAt: StartedAt,
    device: Device,
    client: Client,
    createdAt: CreatedAt,
)
