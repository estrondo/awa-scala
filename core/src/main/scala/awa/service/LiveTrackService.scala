package awa.service

import awa.IO
import awa.input.LiveTrackInput
import awa.model.Account
import awa.model.Track

trait LiveTrackService:

  def add(track: LiveTrackInput, account: Account): IO[Track]
