#include <stdio.h>
#include <jni.h>
#include <parted/parted.h>
#include <vector>

JNIEXPORT jobjectArray JNICALL Java_com_api_deployer_scanners_linux_LinusFSScanner_findDrives(JNIEnv *env, jobject obj) {
    jobjectArray result;

    PedDevice* dev;
    for ( dev = findDevices(); dev; dev = dev->next ) {
        PedDiskType*	disk_type;
        PedDisk*	disk;

        disk_type = ped_disk_probe (dev);
        if (!disk_type || !strcmp (disk_type->name, "loop"))
             return NULL;

        disk = ped_disk_new (dev);
        if (!disk)
             return NULL;

        jobject object = createDiskObject(disk);
    }

    return NULL;
};

std::vector<PedDevice*> findDevices() {
    std::vector<PedDevice*> devices();

}

jobject createDiskObject( JNIEnv* env, PedDisk* disk ) {
    return NULL;
}


