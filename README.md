### customGallery
This is a simple module gives you a possibility to choose photo/images from your android device(except thumbnails).<br>

## Description
 customGallery has two types.
 * First type it's default design which you can see on attached screens.
 * Second type it's your custom type which you create with help GalleryOptions object(description how to use it see below).

### Screenshots
![Screenshot](https://github.com/Alytar/customGallery/blob/master/gallery_screen.png)
![Screenshot](https://github.com/Alytar/customGallery/blob/master/folder_screen_unchecked.png)
![Screenshot](https://github.com/Alytar/customGallery/blob/master/folder_screen_checked.png)
![Screenshot](https://github.com/Alytar/customGallery/blob/master/page_screen.png)

### Requirements
The library requires Android API Level 14+.

## Integration
 * Download and unzip the project you've just downloaded
 * Import the customGallery module in your Android Studio project (File > New > Import Module)
 * Add module to build.gradle
```groovy
  dependencies {
      compile project (':customgallery')
  }
```
* Before using customGallery you need to start service at the beginning of your application for creating custom thumbnails
  CustomGallery.startCreateThumbsService(context);

### Usage
    Add code below in your activity for using and adjusting customGallery
``` java
  //Your call button for the customGallery
    private Button buttonGallery;
    private CustomGallery gallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonGallery = (Button) findViewById(R.id.buttonGallery);
        //Start service for creating custom gallery thumbnails MUST start at the beginning of your application
        CustomGallery.startCreateThumbsService(this);
        //Create options for customGallery(default options)
        GalleryOptions galleryOptions=new GalleryOptions(this);
        //Change color for toolbar
        galleryOptions.setColorToolbar(Color.GREEN);
        //Change color for toolbar when you click on check(small circle in upper right corner on photo)
        galleryOptions.setColorSelectedToolbar(Color.RED);
        //Change main background for customGallery
        galleryOptions.setColorBackground(Color.YELLOW);
        //Change grid background for customGallery
        galleryOptions.setColorBackgroundGridView(Color.BLACK);
        //Change text color on both buttons
        galleryOptions.setTextColorInButtons(Color.BLUE);
        //Change title color in toolbar
        galleryOptions.setTextColorInToolbar(Color.CYAN);
        //Change drawable for buttons
        galleryOptions.setButtonBackground(context.getResources().getDrawable(R.drawable.custom_btn));
        //Change navigation icon in toolbar(the default is arrow)
        galleryOptions.setDrawableNavigationIconToolbar(getResources().getDrawable(R.drawable.abc_btn_check_material));
        //Change drawable under photo
        galleryOptions.setDrawablePlaceHolder(getResources().getDrawable(R.drawable.photo_test));

        //Create customGallery
        //first parameter Context
        //second galleryOptions which can be changed or remain unchanged
        //the third CustomGallery.GalleryClickCallBack() object for to catching event from the button SEND(default text on button)
        gallery = new CustomGallery(this, galleryOptions, new CustomGallery.GalleryClickCallBack() {
                    @Override
                    public void sendClicked(List<String> list) {
                        for (int i = 0; i < list.size(); i++) {
                            Log.e("Log","path "+list.get(i));
                        }
                    }
                });
        //start customGallery on your button
        buttonGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gallery.startGallery();
            }
        });
    }
}
```

Below you can see mine drawable background resource for buttons(SEND, CANCEL) in customGallery.
If you want change it you should create your drawable resource and change gallery options (galleryOptions.setButtonBackground()).
### custom_btn.xml
``` xml
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <!-- pressed state -->
    <item android:state_pressed="true">
            <shape android:shape="rectangle">
                <corners android:radius="@dimen/control_corner_material" />
                <solid android:color="@color/button_pressed_true" />
            </shape>
    </item>
    <!-- normal state -->
    <item>
            <shape android:shape="rectangle">
                <corners android:radius="@dimen/control_corner_material" />
                <solid android:color="@color/button_pressed_false" />
            </shape>
    </item>
</selector>
```
