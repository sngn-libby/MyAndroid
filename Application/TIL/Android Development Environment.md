# Android Development Environment

<br>

[toc]

<br>

- SDK
- Editor (Android Studio)
- AVD
- 실제 Device (phone)



- on으로 시작하는 메서드 -> 생성자에서 호출하는 시스템메서드  
  상태를 저장한다. (다른 작업 수행 후 다시 되돌아올 때 -> 이전에 종료했던 시점부터 수행되는 것 -> 이전 종료시 작업상태를 저장하는 코드가 있었기 때문에)

- 화면을 동적으로 구성하고 싶을 때는 코드로 구현해야한다. (xml은 정적인 구성에서만 가능하다.(template느낌))

  ```java
  /* MainActivity.java */
  
  // 정적구성
  Toast.makeText(this, "a1 Call", Toast.LENGTH_SHORT).show();
  Intent i = new Intent(this, A1_Activity.class);
  startActivity(i); // 페이지전환 component 호출
  
  // 동적구성
  Button b = new Button(this);
  b.setText("hello");
  setContentView(b);
  ```

- activity_main.xml 에 등록되는 버튼, 이미지뷰 등의 id는 **R.java**에 맵형태로 저장된다.

- res 폴더에 들어있는 모든 리소스는 **R.java**에 자동으로 등록된다.

- `onCreate()` 에서 작성하는 코드 (53p)

  - 화면 초기화
  - callBack
  
  



<br>

### 1. app

- src 
  - java
  - res (resourece)
  - manifests : AndroidManifes.xml (환경설정파일)
  - gradle (빌드 환경)

### 2. Intent (67-8p)

- 같은 프로세스(같은앱)에서 다른 컴포넌트를 호출할 때 가장 많이 쓰이는 방법 (명시적호출)

- **카톡 채팅창**에서 플러스아이콘을 누르면 선택창이 나타나고

- 선택창에서 갤러리를 누르면 **앨범**이 실행되고

- 앨범에서 사진을 선택해서 전송하면 **채팅창에 사진이 보내지는 것**

- 위의 모든것이 Intent이다.

  ```java
  // 특정 component 호출시키기 (페이지전환)
  Intent i = new Intent(this, A1_Activity.class);
  startActivity(i);
  ```

- 컴포넌트간(채팅창, 앨범)에 해당 데이터를 공유할 때 Intent 사용한다.

- `startActivityForResult(i, 식별자(requestCode))`

  ```java
  /* MainActivity */
  Intent i = new Intent();
  startActivityForResult(i, requestCode); // ---> NextActivity 호출
  onActivityResult(requestCode, resultCode, mInt); // --> NextActivity에서 finish();되어 진행된다(callback)
  
  /* NextActivity */
  Intent mInt = new Intent();
  setResult(RESULT_OK, mInt); // 결과값 다시 돌려주기
  finish();
  
  
  ```

  - requestCode가 0보다 작으면 --> startActivity와 같은 의미 (결과값을 받지 않겠다.)
  - 0보다 큰 값을 requestCode에 넣어야 한다.

<Br>

### 기본 Structrue (프로젝트 구성하기)

- values diroctory

  - 소문자로시작
  - 이름은 리소스로
  - `colors.xml`

- 모든 Activity는 manifests에 등록된다.

  ```java
  <intent-filter>
      <action android:name="android.intent.action.MAIN" />
      <category android:name="android.intent.category.LAUNCHER" />
  </intent-filter>
  ```

  - 앱의 아이콘을 생성해주고 Entry Point를 설정해준다 (`MAIN, LAUNCHER`)
  - Main의 정보는 하나만 설정하는 것이 원칙이다. 
    : 나머지는 LAUNCHER가 아닌 DEFAULT로 설정하면 된다.
  - `<intent-filter>`를 명시하는 순간 외부에서 호출할 수 있다.



<br>

### 1. 버튼만들기

- 버튼 이름 -> strings.xml에 선언해두고, 사용하기