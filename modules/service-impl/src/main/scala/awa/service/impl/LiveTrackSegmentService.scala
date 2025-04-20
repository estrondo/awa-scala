package awa.service.impl

import awa.*
import awa.generator.KeyGenerator
import awa.input.LiveTrackSegmentInput
import awa.model.Track
import awa.model.TrackSegment
import awa.persistence.TrackSegmentRepository
import awa.service.LiveTrackSegmentService
import io.github.arainko.ducktape.*
import zio.ZIO

object LiveTrackSegmentService:

  def apply(
      keyGenerator: KeyGenerator,
      repository: TrackSegmentRepository,
  ): LiveTrackSegmentService = new LiveTrackSegmentService:
    override def add(input: LiveTrackSegmentInput, track: Track): IO[TrackSegment] = {
      for
        trackSegment <- convert(input, track)
                          .logError("Unable to convert the input.")
        added        <- repository
                          .add(trackSegment)
                          .logError("Unable to add a new segment.")
        _            <- ZIO.logInfo("TrackSegment was added.")
      yield added
    }.annotated(
      "segment.traceId" -> input.traceId,
      "track.id"        -> track.id.value,
      "account.id"      -> track.account.id,
    )

    private def convert(input: LiveTrackSegmentInput, track: Track): IO[TrackSegment] =
      ZIO
        .attempt {
          input
            .into[TrackSegment]
            .transform(
              Field.const(_.id, keyGenerator.generateL16()),
              Field.const(_.order, None),
              Field.const(_.track, track),
            )
        }
        .mapErrorToAwa(AwaException.Unexpected("An expected error has occurred.", _))
