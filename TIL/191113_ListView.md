[toc]

<br>

# View Group

<br>

## Adapter View (100p)

- Adapter View : 데이터를 표출하는 주체  
  Adapter : 데이터를 관리하는 주체 (Database 형태를 가장 많이 사용한다.), Adapter View와 상속관계이다 (자식)
- Base Adapter를 상속받아 Custom Adapter를 만들기

<br>

### 1. List View

- Auto Scroll 생성

- ListView Process (`MainActivity.java`)

  1. 사용할 List View 객체를 가져오기

     ```java
     ListView lv = findViewById(R.id.myListView);
     ```

     

  2. Adapter View에 설정한 Adapter를 만들고

     ```java
     ArrayAdapter<String> foodAdapter 
                     = new ArrayAdapter<String>(
                             this, android.R.layout.simple_list_item_1, foodArr);
     ```

     - row layout을 설정하고 `android.R.layout.simple_list_item_1` (여기서는 android에서 기본제공하는 것을 사용했다.)
     - 데이터를 넘겨준다 `foodArr`

  3. Adapter Veiw에 Adapter를 설정한다.

     ```java
     lv.setAdapter(foodAdapter);
     ```

  4. Event 설정하기

     ```java
     
     ```

  <br>

### 1-1. List View Customization Process

1. 내부(`MainActivity.java`)에 BaseAdapter를 상속받은 Adapter Class 만들기

   ```java
   class MovieAdapter<M> extends BaseAdapter {
       private int layout;
       private ArrayList<Movie> movieList;
   
       public MovieAdapter(int rowLayout, ArrayList<Movie> movieList) {
           this.layout = rowLayout;
           this.movieList = movieList;
       }
   ```

2. BaseAdapter implements Overriding 시키기 ( **getView 만들기** )  
   getView 호출시 --> 리스트뷰의 한 줄씩 출력된다.scroll을 할 때마다 호출되는 개념 (안보이던거 보일때 --> getView로 호출된다.)

   ```java
   @Override
   public View getView(int position, View convertView, ViewGroup parent) {
   
       boolean NORM = false;
       Movie selMovie = (Movie) getItem(position);
       ViewHolder viewHolder;
   
       if(NORM) {
           // normal way
           View nView = View.inflate(MainActivity.this, layout, null);
   
           TextView titleV = nView.findViewById(R.id.mTitleV);
           TextView contentV = nView.findViewById(R.id.mConV);
           ImageView imgV = nView.findViewById(R.id.mImgV);
   
           titleV.setText(selMovie.getmTitle());
           contentV.setText(selMovie.getmContent());
           imgV.setImageResource(selMovie.getmImg());
   
           return nView;
   
       } else {
           if(convertView == null) {
               convertView = View.inflate(MainActivity.this, layout, null);
   
               viewHolder = new ViewHolder();
               viewHolder.mTv = convertView.findViewById(R.id.mTitleV);
               viewHolder.mConTv = convertView.findViewById(R.id.mConV);
               viewHolder.mImgV = convertView.findViewById(R.id.mImgV);
               convertView.setTag(viewHolder);
   
           } else {
               viewHolder = (ViewHolder) convertView.getTag();
           }
   
           TextView titleV = convertView.findViewById(R.id.mTitleV);
           TextView contentV = convertView.findViewById(R.id.mConV);
           ImageView imgV = convertView.findViewById(R.id.mImgV);
   
           viewHolder.mTv.setText(selMovie.getmTitle());
           viewHolder.mConTv.setText(selMovie.getmContent());
           viewHolder.mImgV.setImageResource(selMovie.getmImg());
   
           return convertView;
       }
   }
   ```

   

3. Adapter View에 설정한 Adapter 만들기

   ```java
   ArrayAdapter<String> foodAdapter
                   = new ArrayAdapter<String>(
                           this, R.layout.row_layout, R.id.titleTv);
   ```

4. List Adapter 만들기

   ```java
   setListAdapter(foodAdapter);
   ```

<br>

- 동적으로 화면 구성하기

  - Menu와 Layout 은 XML을 객체화 시킬 수 있다.

  - Inflater 종류 : Menu Inflater, Layout Inflater
  
- **Layout Inflater**  
    : XML로 작성한 layout을 객체화하기 위해 사용한다. (정적 구성시 : `setContentView(R.layout.activity_main);`)
  
    ```java
    // Inflater 사용법
    // 1
    LayoutInflater.from(MainActivity.this).inflate(layout, null);
    // 2
    View.inflate(MainActivity.this, layout, null);
    // 3
  View nView = View.inflate(MainActivity.this, layout, null);
    ```

    
  
    - Layout Inflater 객체 얻어오기 (System에 정의된 객체 가져오는 함수 `getSystemService()`)  
      `Context.getSystemService(Context.LayoutInflater)`
  - method  
      `Inflate(int layoutId, View root)`
  
  ```java
  // 객체화시킬 준비
  LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
  
  // 객체화
  public void showAtype(View view) {
      View aView = layoutInflater.inflate(R.layout.a_layout, null);
  }
  
  // View 추가
  myLinear.removeAllViews();
myLinear.addView(aView);
  ```
  
  

<br>

### 2. Spinner

<br>

### 3. Gallery

<br>

### 4. Grid View

- GridView도 Adpater View이다.

