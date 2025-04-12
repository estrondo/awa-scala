package awa.input

import awa.data.PositionData
import awa.data.TagMap
import awa.model.Account
import java.time.ZonedDateTime
import org.locationtech.jts.geom.Point

case class LiveTrackPositionInput(
    traceId: String,
    account: Account,
    tagMap: TagMap,
    startedAt: ZonedDateTime,
    deviceId: Option[String],
    deviceType: Option[String],
    point: Point,
    positionData: PositionData,
)
