![combobox-demo](https://user-images.githubusercontent.com/6856748/117393536-eb865700-af26-11eb-970d-5d92d3d6976a.gif)
# ComboBox widget for android
A ComboBox Widget just like Spinner for Android 

![alt tag](combbox-demo.gif)

### Usage

The usage is pretty straightforward. Add the tag into the XML layout:
```xml
 <com.lyd.box.ComboBox
   android:id="@+id/box"
   android:layout_width="wrap_content"
   android:layout_height="wrap_content"
   android:layout_margin="16dp"/>
```
 Then use this snippet to populate it with contents:
```java
 ComboBox comb = (ComboBox) findViewById(R.id.box);
 List<String> dataset = new LinkedList<>();
 dataset.add("Menu1");
 dataset.add("Menu2");
 dataset.add("Menu3");
 dataset.add("Menu4");
 dataset.add("Menu5");
 dataset.add("Menu6");
 box.setDataSet(dataset);
```
 You can eanable edit mode or not with contents:
```java
 bool editable = false;
 box.setEditable(editable)
```
#### Listeners
For listening to the item selection actions, you can just use the following snippet:
```java
spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
    @Override
    public void onItemSelected(ComboBox parent, View view, int position, long id) {
        String item = parent.getItemAt(position);
        ...
    }
});
```

#### Attributes
You can add attributes to customize the view. Available attributes:

| name                      | type      | info                                                   |
|------------------------   |-----------|--------------------------------------------------------|
| arrowTint                 | color     | sets the color on the drop-down arrow                  |
| hideArrow                 | boolean   | set whether show or hide the drop-down arrow           |
| arrowDrawable             | reference | set the drawable of the drop-down arrow                |
| textTint                  | color     | set the text color                                     |
| dropDownListPaddingBottom | dimension | set the bottom padding of the drop-down list           |
| backgroundSelector        | integer   | set the background selector for the drop-down list rows |
| popupTextAlignment        | enum      | set the horizontal alignment of the default popup text |
| entries                   | reference | set the data source from an array of strings |

How to include
---

With gradle: edit your `build.gradle`:
```groovy
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}

dependencies {
   implementation 'com.github.MachealLu:AComboBox:v1.0.0'
}

