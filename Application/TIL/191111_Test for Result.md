# Test for Result

<br>

[toc]

<br>

<br> 

### 1. TextView를 가리키는 변수 만들기

```xml
// activity_main.xml
// TextView의 id를 infoTv로 만들기
<TextView
          android:id="@+id/infoTv"
```



```java
// MainActivity.java
TextView infoTv;

@Override
protected void onConcrete(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    infoTv = findViewById(R.id.infoTv);
}
```

<br>

### 2. edit 버튼을 누르면 edit 창 띄우기

```xml
<!-- activity_main.xml 
	: Button에 onClick속성 만들기 -->

<Button
        android:onClick="edit"
```



```java
// MainActivity.java
public static final EDIT_MODE = 1;
public void edit(View view) {
    Intent i = new Intent("com.sm.android.section.EDIT");
    
    String str = infoTv.getText().toString();
    i.putExtra("data", str);
    startActivityForResult(i, EDIT_MODE);
}
```

<br>

### 3. "com.sm.android.section.EDIT" token을 인식할 수 있는 intent-filter설정하기

```xml
<!-- AndroidManifest.xml -->
<activity android:name=".EditActivity">
    <intent-filter>
    	<action android:name="com.sm.android.section.EDIT" />
        <category android:name="android.intent.category.DEFAULT" />
    </intent-filter>
</activity>
```

<br>

### 4. EditActivity.java : *MainActivity.java에서 보낸 intent 받기* + 수정하기

```java
EditText editTv; // 수정텍스트상자만들기

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edit);
    
    Intent recInt = getIntent();
    String str = recInt.getStringExtra("data");
    
    editTv = fineViewById(R.id.editTv);
    editTv.setText(str);
}
```

<br>

### 5. save 버튼 클릭시 Main에 수정된 데이터 보내기

```java
// EditActivity.java
public void save(View view) {
    Intent i = new Intent();
    
    String str = editTv.getText().toString();
    i.putExtra("data", str);
    
    setResult(RESULT_OK, i);
    finish();
}

public void cancel(View view) {
    setResult(RESULT_CANCELED);
    finish();
}
```

<br>

### 6. EditActivity.java 에서 보낸 데이터 받아서 MainActivity.java에 반영하기

```java
@Override
protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if(resultCode == RESULT_OK && requestCode == EDIT_MODE)
        infoTv.setText(data.getStringExtra("data")); // 화면에 반영하기
}
```

