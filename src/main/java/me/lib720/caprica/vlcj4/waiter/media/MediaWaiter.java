/*
 * This file is part of VLCJ.
 *
 * VLCJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VLCJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VLCJ.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009-2019 Caprica Software Limited.
 */

package me.lib720.caprica.vlcj4.waiter.media;

import me.lib720.caprica.vlcj4.waiter.Waiter;
import me.lib720.caprica.vlcj4.media.Media;
import me.lib720.caprica.vlcj4.media.MediaEventAdapter;
import me.lib720.caprica.vlcj4.media.MediaEventListener;
import me.lib720.caprica.vlcj4.media.MediaParsedStatus;
import me.lib720.caprica.vlcj4.media.MediaRef;
import me.lib720.caprica.vlcj4.media.Meta;
import me.lib720.caprica.vlcj4.media.Picture;
import me.lib720.caprica.vlcj4.player.base.State;

/**
 * Base implementation for a conditional waiter for media events.
 *
 * @param <R> type of result that may be returned when the desired condition arises
 */
abstract public class MediaWaiter<R> extends Waiter<Media, R> implements MediaEventListener {

    /**
     * Internal event listener used to fire finished/error completion statuses.
     * <p>
     * This additional listener is used so sub-classes can freely override the default empty listener methods without
     * interfering with proper operation of this component.
     */
    private final MediaEventListener internalListener = new InternalListener();

    /**
     * Create a new conditional waiter for media.
     *
     * @param component component to wait for
     */
    protected MediaWaiter(Media component) {
        super(component);
    }

    @Override
    protected final void startListening(Media component) {
        component.events().addMediaEventListener(internalListener);
        component.events().addMediaEventListener(this);
    }

    @Override
    protected final void stopListening(Media component) {
        component.events().removeMediaEventListener(internalListener);
        component.events().removeMediaEventListener(this);
    }

    private class InternalListener extends MediaEventAdapter {
        @Override
        public void mediaStateChanged(Media media, State newState) {
            switch (newState) {
                case ENDED:
                    MediaWaiter.super.finished();
                    break;
                case ERROR:
                    MediaWaiter.super.error();
                    break;
            }
        }
    }

    // === MediaEventListener ==================================================

    @Override
    public void mediaMetaChanged(Media media, Meta metaType) {
    }

    @Override
    public void mediaSubItemAdded(Media media, MediaRef newChild) {
    }

    @Override
    public void mediaDurationChanged(Media media, long newDuration) {
    }

    @Override
    public void mediaParsedChanged(Media media, MediaParsedStatus newStatus) {
    }

    @Override
    public void mediaFreed(Media media, MediaRef mediaFreed) {
    }

    @Override
    public void mediaStateChanged(Media media, State newState) {
    }

    @Override
    public void mediaSubItemTreeAdded(Media media, MediaRef item) {
    }

    @Override
    public void mediaThumbnailGenerated(Media media, Picture picture) {
    }

}
