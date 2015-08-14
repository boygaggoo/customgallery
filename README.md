### customGallery only photo
![Screenshot](https://github.com/frontiertsymbal/emoji_keyboard/blob/master/EmojiKeyboard.png)
## Usage
### YourActivity.java
``` java
  //Your call button for the customGallery
    private Button buttonGallery;

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
        final CustomGallery gallery = new CustomGallery(this, galleryOptions, new CustomGallery.GalleryClickCallBack() {
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
### custom_btn.xml
You can create your background drawable for buttons. Below you can see mine drawable
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
