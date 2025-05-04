package awa.model

import awa.model.data.AccountId
import awa.model.data.CreatedAt
import awa.model.data.DeviceId
import awa.model.data.DeviceType
import awa.model.data.StartedAt
import awa.model.data.TrackId

case class Track(
    id: TrackId,
    accountId: AccountId,
    startedAt: StartedAt,
    deviceId: DeviceId,
    deviceType: DeviceType,
    createdAt: CreatedAt,
)
