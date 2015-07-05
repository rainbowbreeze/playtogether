/*
 * Copyright 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
   -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ */

package it.rainbowbreeze.playtog.common;

/**
 * Created by alfredomorresi on 19/10/14.
 */
public abstract class SharedBag {
    public final static String APP_NAME_LOG = "PlayTogether";


    public final static String WEAR_PATH_AMAZINGPICTURE = "/AmazingPicture";
    public final static String WEAR_PATH_REMOVEPICTURE = "/RemovePicture";
    public final static String WEAR_PATH_UPLOADPICTURE = "/UploadPicture";
    public final static String WEAR_PATH_OPENPICTURE = "/OpenPicture";

    public final static String WEAR_DATAMAPITEM_PICTUREID = "PictureId";
    public static final String WEAR_DATAMAPITEM_TIMESTAMP = "Timestamp"; // Avoids caching

    // Also registered for the service
    public final static String INTENT_ACTION_REMOVEPICTURE = "it.rainbowbreeze.picama.Action.Picture.Remove";
    public static final String INTENT_ACTION_UPLOADPICTURE = "it.rainbowbreeze.picama.Action.Picture.Upload";
    public static final String INTENT_ACTION_OPENONDEVICE = "it.rainbowbreeze.picama.Action.Picture.OpenOnDevice";
    public final static String INTENT_EXTRA_PICTUREID = "Extra.PictureId";
    public final static String INTENT_EXTRA_NOTIFICATIONID = "Extra.NotificationId";

    public static final long ID_NOT_SET = -1;
    public static final long GOOGLE_API_CLIENT_TIMEOUT = 30;  // seconds
}
