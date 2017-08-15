package apt.tutorial;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class Photographer extends Activity {
	private SurfaceView preview=null;
	private SurfaceHolder previewHolder=null;
	private Camera camera=null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photographer);
		
		preview=(SurfaceView)findViewById(R.id.preview);
		previewHolder=preview.getHolder();
		previewHolder.addCallback(surfaceCallback);
		previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode==KeyEvent.KEYCODE_CAMERA ||
				 keyCode==KeyEvent.KEYCODE_SEARCH) {
			 takePicture();
			 
			 return(true);
		}
		
		return(super.onKeyDown(keyCode, event));
	}
	
	private void takePicture() {
		 camera.takePicture(null, null, photoCallback);
	}
	
	SurfaceHolder.Callback surfaceCallback=new SurfaceHolder.Callback() {
		public void surfaceCreated(SurfaceHolder holder) {
			camera=Camera.open();
			
			try {
				camera.setPreviewDisplay(previewHolder);
			}
			catch (Throwable t) {
				Log.e("Photographer",
							"Exception in setPreviewDisplay()", t);
				Toast
					.makeText(Photographer.this, t.getMessage(),
										Toast.LENGTH_LONG)
					.show();
			}
		}
		
		public void surfaceChanged(SurfaceHolder holder,
															 int format, int width,
															 int height) {
			Camera.Parameters parameters=camera.getParameters();
			
			parameters.setPreviewSize(width, height);
			parameters.setPictureFormat(PixelFormat.JPEG);
			
			camera.setParameters(parameters);
			camera.startPreview();
		}
		
		public void surfaceDestroyed(SurfaceHolder holder) {
			camera.stopPreview();
			camera.release();
			camera=null;
		}
	};
	
	Camera.PictureCallback photoCallback=new Camera.PictureCallback() {
		 public void onPictureTaken(byte[] data, Camera camera) {
			 // do something with the photo JPEG (data[]) here!
			 camera.startPreview();
		 }
	};
}
