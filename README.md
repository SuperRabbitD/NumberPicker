# WheelPicker
The project contains a customizable WheelPicker which is more flexible than android native NumberPicker widget. 
## Features
- Customizable font include text size, typeface, color and alignment.
- Customizable picker item count, can be odd or even number.
- Support both positive and negative values
- Support different picker style, like limited picker, rounded wrapped picker and unlimited picker.
- Allow users to set their adapters for supportting different requirements. The video below shows an implementation of date picker by pluging custom date adapter into the wheel picker.
### Attributes
|attribute name|attribute description|
|:-:|:-:|
|fadingEdgeEnabled|enable/disable the fading edge attribute.|
|max|The max index(value) of the picker, can be negative, default is Integer.MAX_VALUE.|
|min|The minimum index(value) of the picker, can be negative, default is Integer.MIN_VALUE|
|selectedTextColor|The text color of selected item.|
|textColor|The text color of unselected item.|
|textSize|Text size.|
|typeface|Text font typeface.|
|wheelItemCount|The visible item count of the picker.|
|wrapSelectorWheel|true if the picker is rounded wrapped, default is false.|
|align|The text alignment, can be LEFT, CENTER, RIGHT.|
## Usage
#### Kotlin
```Kotlin
// Set rounded wrap enable
numberPicker.setSelectorRoundedWrapPreferred(true)
// Set wheel item count
numberPicker.setWheelItemCount(5)
// Set wheel max index
numberPicker.setMax(1000)
// Set wheel min index
numberPicker.setMax(1000)
// Set selected text color
numberPicker.setSelectedTextColor(R.color.color_4_blue)
// Set unselected text color
numberPicker.setUnselectedTextColor(R.color.color_3_dark_blue)
//Set user defined adapter
numberPicker.setAdapter(WPDayPickerAdapter())

// OnValueChangeListener
val context = this
numberPicker.setOnValueChangeListener(object : OnValueChangeListener{
  override fun onValueChange(picker: WheelPicker, oldVal: String, newVal: String) {
    val out = String.format("Current: %s", newVal)
    Toast.makeText(context, out, Toast.LENGTH_SHORT).show()
  }
})

// Adapter sample 
/**
 * Custom wheel picker adapter for implementing a date picker
 */
class WPDayPickerAdapter : WheelAdapter {
    //get item value based on item position in wheel
    override fun getValue(position: Int): String {
        if (position == 0)
            return "Today"

        if(position == -1)
            return "Yesterday"

        if (position == 1)
            return "Tomorrow"

        val curDate = Date(System.currentTimeMillis())
        val rightNow = Calendar.getInstance()
        rightNow.time = curDate;
        rightNow.add(Calendar.DATE, position)
        
        val simpleDateFormat = SimpleDateFormat("MMM d, yyyy")
        return simpleDateFormat.format(rightNow.time)
    }

    //get item position based on item string value
    override fun getPosition(vale: String): Int {
        return 0
    }

    //return a string with the approximate longest text width, for supporting WRAP_CONTENT
    override fun getTextWithMaximumLength(): String {
        return "Mmm 00, 0000"
    }

    //return the maximum index
    override fun getMaxIndex(): Int {
        return Integer.MAX_VALUE
    }

    //return the minimum index
    override fun getMinIndex(): Int {
        return Integer.MIN_VALUE
    }
}
```
## Demos
![image](https://github.com/SuperRabbitD/WheelPicker/blob/master/gif_demo/demo_wrap.gif)
![image](https://github.com/SuperRabbitD/WheelPicker/blob/master/gif_demo/demo_change_color.gif)
![image](https://github.com/SuperRabbitD/WheelPicker/blob/master/gif_demo/demo_change_item_count.gif)
![image](https://github.com/SuperRabbitD/WheelPicker/blob/master/gif_demo/demo_date_picker.gif)


