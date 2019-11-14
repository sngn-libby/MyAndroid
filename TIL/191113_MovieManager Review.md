# MovieManager.java

<br>

[toc]

<br>

### 1. Movie 정보를 담을 class 생성

```java
public class Movie {
    private String mTitle, mContent;
    private int mImg; //resource Item 이기때문에 --> R.img.drawable 이런식으로

    public Movie(String mTitle, String mContent, int mImg) {
        this.mTitle = mTitle;
        this.mContent = mContent;
        this.mImg = mImg;
    }
    
    // Getter, Setter 설정
}
```

<br>

### 2. 데이터 풀을 받을 List View 만들기

```java
ListView mListV;

mListV = findViewById(R.id.movieListView);
```

<br>

### 3. Inner Class 만들기 : ViewHolder 

```java
class ViewHolder {
    TextView mTv, mConTv;
    ImageView mImgV;
}
```

<br>

### 4. MovieAdapter 만들기

```java
class MovieAdapter<M> extends BaseAdapter {
    private int layout;
    private ArrayList<Movie> movieList;

    public MovieAdapter(int rowLayout, ArrayList<Movie> movieList) {
        this.layout = rowLayout;
        this.movieList = movieList;
    }
```

<br>

### 5. BaseAdapter  상속 구현하기(Override)

```java
@Override
public int getCount() {  return movieList.size(); }

@Override
public Object getItem(int position) { return movieList.get(position); }

@Override
public long getItemId(int position) {  return position; }

@Override
public View getView(int position, View convertView, ViewGroup parent) {

    Movie selMovie = (Movie) getItem(position);
    ViewHolder viewHolder;

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
    viewHolder.mTv.setText(selMovie.getmTitle());
    viewHolder.mConTv.setText(selMovie.getmContent());
    viewHolder.mImgV.setImageResource(selMovie.getmImg());

    return convertView;
    
}
```

- `getView()` 뜯어보기

  1. 불러올 객체 설정

     ```java
     Movie selMovie = (Movie) getItem(position);
     ```

  2. View 객체 화면과 연동

     ```java
     ViewHolder viewHolder;
     
     // 초기에 한번만 설정
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
     ```

  3. View 내용 설정

     ```java
     viewHolder.mTv.setText(selMovie.getmTitle());
                     viewHolder.mConTv.setText(selMovie.getmContent());
                     viewHolder.mImgV.setImageResource(selMovie.getmImg());
     ```

  4. 리턴 View

     ```java
     return convertView;
     ```

  5. main 사용법

     ```java
     MovieAdapter<Movie> movieAdapter = new MovieAdapter<>(R.layout.row_layout, getAllInfo());
     mListV.setAdapter(movieAdapter);
     ```

     





