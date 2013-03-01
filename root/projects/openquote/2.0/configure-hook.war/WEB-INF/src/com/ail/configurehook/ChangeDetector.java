/* Copyright Applied Industrial Logic Limited 2013. All rights reserved. */
/*
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later 
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or 
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51 
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package com.ail.configurehook;

import java.util.ArrayList;
import java.util.List;

import com.liferay.portlet.documentlibrary.model.DLFileEntry;

class ChangeDetector {
    private static final int HISTORY_MAX_SIZE = 20;
    private List<DLFileEntry> history = new ArrayList<DLFileEntry>(HISTORY_MAX_SIZE);
    private int nextWriteIndex = 0;

    ChangeDetector() {
    }

    void record(final DLFileEntry fileEntry) {
        if (!history.contains(fileEntry)) {
            history.add(nextWriteIndex, fileEntry);
            nextWriteIndex = (nextWriteIndex + 1) % HISTORY_MAX_SIZE;
        }
    }
    
    boolean isChanged(final DLFileEntry fileEntry) {
        for (int i = 0; i < HISTORY_MAX_SIZE; i++) {
            DLFileEntry element = history.get(i);
            if (element.hashCode() == fileEntry.hashCode()) {
                if (element.getVersion().equals(fileEntry.getVersion())) {
                    return false;
                } else {
                    history.set(i, fileEntry);
                    return true;
                }
            }
        }

        // Theoretically, we should never reach this point.
        throw new IllegalStateException("History contains element " + fileEntry.getTitle() + ", but element could not be found.");
    }
}
