//package com.cuongngo.core_project.ui.login.take_photo
//
//class TakeFacePhotoActivity {
//
//
//
//
//
//    private fun openCamera() {
//        // Determine the correct permission based on the Android version
//        val storagePermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            android.Manifest.permission.READ_MEDIA_IMAGES
//        } else {
//            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
//        }
//
//        tedPermission(
//            storagePermission,
//            android.Manifest.permission.CAMERA
//        ) {
//            // If permissions are granted, proceed to open the front camera
//            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//
//            // Find the front camera ID
//            val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
//            val frontCameraId = cameraManager.cameraIdList.firstOrNull { cameraId ->
//                cameraManager.getCameraCharacteristics(cameraId)
//                    .get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT
//            }
//
//            // Set the front camera as the active camera
//            if (frontCameraId != null) {
//                cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", CameraCharacteristics.LENS_FACING_FRONT)
//                cameraIntent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1)
//                cameraIntent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true)
//            }
//
//            // Start the camera activity
//            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
//        }
//    }
//
//
//}