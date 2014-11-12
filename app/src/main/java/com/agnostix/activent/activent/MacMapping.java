package com.agnostix.activent.activent;

import android.provider.BaseColumns;

/**
 * Created by Shivam on 11/11/2014.
 */
public abstract class MacMapping {

    public static final String[][] macMap = new String[][]{
            //Academic Block
            {"28:94:0f:d6:d8:3d", "Glass Room", "Academic Block"},
            {"c4:64:13:11:42:17", "CDX", "Academic Block"},
            {"28:94:0f:d6:d8:85", "Academic Block 1st Floor A Wing", "Academic Block"},
            {"c4:64:13:11:43:ba", "Academic First Floor Middle", "Academic Block"},
            {"c4:64:13:11:3f:c0", "Academic Block 1st Floor B Wing", "Academic Block"},
            {"28:94:0f:d6:d8:13", "New Sever Room", "Academic Block"},
            {"c4:64:13:11:40:5e", "Academic Block 2nd floor A Wing", "Academic Block"},
            {"c4:64:13:00:52:f5", "Academic Block Second Floor Middle", "Academic Block"},
            {"28:94:0f:d6:d7:2e", "Academic Block 2nd floor B Wing", "Academic Block"},
            {"28:94:0f:d6:d8:c1", "Academic Block 3rd Floor B Wing", "Academic Block"},
            {"c4:64:13:00:52:61", "Academic Third Floor Middle", "Academic Block"},
            {"c4:64:13:11:3f:1b", "Academic Block 3rd Floor A Wing", "Academic Block"},
            {"28:94:0f:c9:2a:fe", "Academic Block 4th Floor B Wing", "Academic Block"},
            {"c4:64:13:00:52:76", "Academic Forth Floor Middle side", "Academic Block"},
            {"28:94:0f:d6:d0:cb", "Academic Block 4th Floor A Wing", "Academic Block"},
            {"28:94:0f:d6:d8:12", "New PHD LAB 4th floor A wing", "Academic Block"},
            {"c4:64:13:11:44:5a", "Academic Block Fifth Floor A Wing", "Academic Block"},
            {"c4:64:13:11:39:f4", "Academic Block Fifth Floor B Wing", "Academic Block"},
            {"c4:64:13:11:3e:43", "Academic Block 5'th Floor Middle", "Academic Block"},


            //Academic Lecture block
            {"c4:64:13:5b:7e:68", "C01", "Acad Lecture Block"},
            {"c4:64:13:11:3b:c6", "C01", "Acad Lecture Block"},
            {"18:e7:28:a8:30:b6", "C01", "Acad Lecture Block"},
            {"18:e7:28:a8:30:69", "C01", "Acad Lecture Block"},
            {"28:94:0f:c9:28:fd", "C02", "Acad Lecture Block"},
            {"28:94:0f:c9:29:b2", "C03", "Acad Lecture Block"},
            {"28:94:0f:d6:d7:bd", "C11", "Acad Lecture Block"},
            {"28:94:0f:c9:2a:59", "C11", "Acad Lecture Block"},
            {"c4:64:13:11:3d:60", "C12", "Acad Lecture Block"},
            {"28:94:0f:d6:d8:86", "C13", "Acad Lecture Block"},
            {"c4:64:13:2b:e0:0b", "C21", "Acad Lecture Block"},
            {"c4:64:13:11:40:88", "C21", "Acad Lecture Block"},
            {"c4:64:13:11:40:62", "C22", "Acad Lecture Block"},
            {"c4:64:13:11:41:d7", "C23", "Acad Lecture Block"},
            {"28:94:0f:d6:d8:63", "C24", "Acad Lecture Block"},

            //Library
            {"28:94:0f:c9:29:b8", "Library Ground Floor", "Library"},
            {"28:94:0f:d6:d7:9a", "Library Ground Floor", "Library"},
            {"28:94:0f:d6:d7:e9", "Library Ground Floor", "Library"},
            {"c4:64:13:11:40:90", "Library First Floor", "Library"},
            {"c4:64:13:00:55:ba", "Library First Floor", "Library"},
            {"c4:64:13:00:54:c2", "Library First Floor", "Library"},
            {"28:94:0f:c9:2a:d5", "Library L23", "Library"},
            {"c4:64:13:11:3f:f8", "Library L21", "Library"},
            {"c4:64:13:00:53:ea", "Library L22", "Library"},
            {"18:e7:28:35:fb:be", "Library L21", "Library"},
            {"18:e7:28:f0:0e:f8", "Library L22", "Library"},
            {"18:e7:28:35:ed:09", "Library L23", "Library"},
            {"c4:64:13:11:3b:dd", "Library L33", "Library"},
            {"c4:64:13:00:50:75", "Library L31", "Library"},
            {"28:94:0f:d6:d7:b6", "Library 3rd Floor", "Library"},

            //Service Block
            {"18:e7:28:11:69:09", "Service Block Ground Floor","Service Block"},
            {"18:e7:28:d1:83:d6", "Service Block 2nd Floor", "Service Block"},

            //Dining Area
            {"28:94:0f:d6:d8:70", "Canteen", "Student Centre"},
            {"28:94:0f:d6:d6:d7", "Canteen", "Student Centre"},
            {"c4:64:13:11:43:41", "Canteen", "Student Centre"},
            {"c4:64:13:11:43:a1", "Mess", "Student Centre"},
            {"c4:64:13:11:3f:b8", "M Tech Lab", "Student Centre"},
            {"c4:64:13:00:55:45", "Student Centre 3rd Floor", "Student Centre"},

            //Girls Hostel
            {"c4:64:13:5b:7e:58", "Girls Hostel Ground Floor C Wing", "Girls Hostel"},
            {"28:94:0f:c9:29:d0", "Girls Hostel Ground Floor A Wing", "Girls Hostel"},
            {"c4:64:13:11:43:35", "Girls Hostel First Floor A Wing", "Girls Hostel"},
            {"c4:64:13:11:3b:97", "Girls Hostel First Floor B Wing", "Girls Hostel"},
            {"c4:64:13:11:42:69", "Girls Hostel First Floor C Wing", "Girls Hostel"},
            {"c4:64:13:5b:7e:46", "Girls Hostel Second Floor B Wing", "Girls Hostel"},
            {"c4:64:13:00:52:74", "Girls Hostel Second Floor A Wing", "Girls Hostel"},
            {"28:94:0f:d6:d6:22", "Girls Hostel Second Floor C Wing", "Girls Hostel"},
            {"c4:64:13:11:43:74", "Girls Hostel Third Floor A Wing", "Girls Hostel"},
            {"c4:64:13:11:3b:b6", "Girls Hostel Third Floor B Wing", "Girls Hostel"},
            {"28:94:0f:d6:d6:59", "Girls Hostel Third Floor C Wing", "Girls Hostel"},
            {"18:e7:28:af:70:d8", "Girls Hostel Third Floor A Wing", "Girls Hostel"},
            {"18:e7:28:1a:36:f6", "Girls Hostel Third Floor C Wing", "Girls Hostel"},
            {"18:e7:28:af:71:3f", "Girls Hostel 4th Floor A Wing", "Girls Hostel"},
            {"c4:64:13:00:55:56", "Girls Hostel 4th Floor C Wing", "Girls Hostel"},
            {"18:e7:28:d1:84:1c", "Girls Hostel 4th Floor A Wing", "Girls Hostel"},
            {"18:e7:28:d1:83:97", "Girls Hostel 4th Floor C Wing", "Girls Hostel"},

            //Boys Hostel
            {"cc:ef:48:8f:98:45", "Boys Hostel Ground Floor A Wing", "Boys Hostel"},
            {"c4:64:13:00:52:f6", "Boys Hostel Ground Floor C Wing", "Boys Hostel"},
            {"c4:64:13:2b:e0:3a", "Boys Hostel Ground Floor C Wing", "Boys Hostel"},
            {"18:e7:28:f0:0f:15", "Common Room", "Boys Hostel"},            //Deliberate duplicate entry for the common room
            {"c4:64:13:2b:e0:09", "Boys Hostel First Floor A Wing", "Boys Hostel"},
            {"28:94:0f:d6:d8:6e", "Boys Hostel First Floor B Wing", "Boys Hostel"},
            {"c4:64:13:00:54:fc", "Boys Hostel First Floor C Wing", "Boys Hostel"},
            {"28:94:0f:c9:2a:f8", "Boys Hostel First Floor C Wing", "Boys Hostel"},
            {"18:e7:28:a8:30:c3", "Boys Hostel First Floor A Wing", "Boys Hostel"},
            {"18:e7:28:af:71:d8", "Boys Hostel First Floor C Wing", "Boys Hostel"},
            {"c4:64:13:11:43:9e", "Boys Hostel 2nd Floor A Wing", "Boys Hostel"},
            {"c4:64:13:5b:7e:6f", "Boys Hostel 2nd Floor B Wing", "Boys Hostel"},
            {"c4:64:13:11:43:b6", "Boys Hostel 2nd Floor C Wing", "Boys Hostel"},
            {"28:94:0f:d6:d5:c8", "Boys Hostel 2nd Floor C Wing", "Boys Hostel"},
            {"18:e7:28:af:71:51", "Boys Hostel 2nd Floor A Wing", "Boys Hostel"},
            {"18:e7:28:d1:83:de", "Boys Hostel 2nd Floor C Wing", "Boys Hostel"},
            {"28:94:0f:d6:d5:e1", "Boys Hostel 3rd Floor A Wing", "Boys Hostel"},
            {"28:94:0f:c9:2a:0a", "Boys Hostel 3rd Floor B Wing", "Boys Hostel"},
            {"28:94:0f:d6:d8:26", "Boys Hostel 3rd Floor C Wing", "Boys Hostel"},
            {"28:94:0f:d6:d8:ec", "Boys Hostel 3rd Floor C Wing", "Boys Hostel"},
            {"18:e7:28:af:71:d7", "Boys Hostel 3rd Floor A Wing", "Boys Hostel"},
            {"18:e7:28:a8:30:99", "Boys Hostel 3rd Floor C Wing", "Boys Hostel"},
            {"c4:64:13:2b:e0:08", "Boys Hostel 4th Floor A Wing", "Boys Hostel"},
            {"c4:64:13:11:41:2d", "Boys Hostel 4th Floor B Wing", "Boys Hostel"},
            {"28:94:0f:d6:d8:79", "Boys Hostel 4th Floor C Wing", "Boys Hostel"},
            {"c4:64:13:11:41:1d", "Boys Hostel 4th Floor C Wing", "Boys Hostel"},
            {"18:e7:28:af:71:d5", "Boys Hostel 4th Floor A Wing", "Boys Hostel"},
            {"18:e7:28:af:72:1d", "Boys Hostel 4th Floor C Wing", "Boys Hostel"},
            {"c4:64:13:11:43:c5", "Boys Hostel 5th Floor A Wing", "Boys Hostel"},
            {"c4:64:13:11:42:eb", "Boys Hostel 5th Floor C Wing", "Boys Hostel"},
            {"28:94:0f:d6:d8:de", "Boys Hostel 5th Floor C Wing", "Boys Hostel"},
            {"18:e7:28:af:71:48", "Boys Hostel 5th Floor A Wing", "Boys Hostel"},
            {"18:e7:28:af:72:31", "Boys Hostel 5th Floor C Wing", "Boys Hostel"},
            {"28:94:0f:d6:d8:05", "Boys Hostel 6th Floor A Wing", "Boys Hostel"},
            {"c4:64:13:2b:e0:0e", "Boys Hostel 6th Floor C Wing", "Boys Hostel"},
            {"c4:64:13:11:43:b2", "Boys Hostel 6th Floor C Wing", "Boys Hostel"},
            {"18:e7:28:d1:83:6f", "Boys Hostel 6th Floor A Wing", "Boys Hostel"},
            {"18:e7:28:a8:29:42", "Boys Hostel 6th Floor C Wing", "Boys Hostel"}

    };


}
