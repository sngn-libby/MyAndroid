[TOC]

<br>

# 1. DeviceControlActivity.java

- this Activity provides the user interface **to connect, display data, and display [GATT](https://developer.android.com/reference/android/bluetooth/BluetoothGatt.html) services and characteristics** supported by the device.
- this Activity communicates with BluetoothLeService.java

<br>

### 0) Define Variables

```java
private final static String TAG = DeviceControlActivity.class.getSimpleName();

public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

private TextView mConnectionState;
private TextView mDataField;
private String mDeviceName;
private String mDeviceAddress;
private ExpandableListView mGattServicesList;
private BluetoothLeService mBluetoothLeService;
private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
private boolean mConnected = false;
private BluetoothGattCharacteristic mNotifyCharacteristic;

private final String LIST_NAME = "NAME";
private final String LIST_UUID = "UUID";
```



<br>

### 1) Manage Service Lifecycle

<br>

```java
private final ServiceConnection mServiceConnection =  new ServiceConnection()  {
  @Override
  public void onServiceConnected(ComponentName componentName, IBinder service) {
    mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
    if(!mBluetoothLeService.initialize()) finish();

    mBluetoothLeService.connect(mDeviceAddress);
  }
  
  @Override
  public void onServiceDisconnected(ComponentName componentName) {
    mBluetoothLeService = null;
  }
}
```

- IBinder : 서비스와 컴포넌트 사이의 인터페이스를 정의

<br>

### 2) [Thread 사이 메세지 전달](https://itmining.tistory.com/6)

- UI자원에 MainThread와 Sub Thread가 동시 접근하여 동기화 이슈를 발생시키는 것을 방지시키기 위해 **UI 자원 사용은 UI Thread에서만 가능**하게 되어있다.

  1. `Activity.runOnUiThread()`  
     Runs the specified action on the UI thread. If the current thread is the UI thread, then the action is executed immediately. If the current thread is not the UI thread, the action is posted to the event queue of the UI thread.

     `Handler.post()`

  2. `View.post()`

  3. AsyncTask 클래스

     1. execute() 명령어를 통해 AsyncTask 실행
     2. AsyncTask로 백그라운드 작업 실행 전, *MainThread::*onPreExecuted() 가 실행된다. (onResume() 과 같은 역할)
     3. 백그라운드 작업 수행  
        doInBackground() --> **publishProgress()**로 작업확인가능
     4. *MainThread::*onProgressUpdate()는 publishProgress()가 호출될때 자동으로 콜백되는 메서드이다.
     5. doInBackground()에서 작업이 끝나면 *MainThread::*onPostExcuted()로 결과값을 리턴. (onPause() 와 같은 역할)

<br>

### 3) Iterate through GATT Services/Characteristics

```java
private void displayGattService(List<BluetoothGattService> gattServices) {
  
  if(gattServices == null) return;
  String uuid = null;
  String unkownwServiceString = "Unknown service";
  String unknownCharacterString = "Unknown characteristic";
  ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<>();
  ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData = new ArrayList<>();
  mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
  
  // Loops through available GATT Services.
  for(BluetoothGattService gattService : gattServices) {
    HashMap<String, String> currentServiceData = new HashMap<>();
    uuid = gattService.getUuid().toString();
    currentServiceData.put(
    	LIST_NAME, SampleGattAtributes.lookup(uuid, unknownServiceString));
    currentServiceData.put(LIST_UUID, uuid);
    gattServiceData.add(currentServiceData);
    
    ArrayList<HashMap<String, String>> gattCharacteristicGroupData = new ArrayList<>();
    List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
    ArrayList<BluetoothGattCharacteristic> charas = new ArrayList<>();
    }
  
  // Loops through available Characteristics
  for(BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
    charas.add(gattCharacteristic);
    HashMap<String, String> currentCharaData = new HashMap<>();
    uuid =  gattCharacteristic.getUuid().toString();
    currentCharaData.put(LIST_NAME, SampleGattAttributes.lookup(uuid, unknownCharaString));
    currentCharaData.put(LIST_UUID, uuid);
    gattCharacteristicGroupData.add(currentCharaData);
  }
  mGattCharacteristics.add(charas);
  gattCharacteristicData.add(gattCharacteristicGroupData);
}
```



<br>

# 2. DeviceScanActivity.java

*Activity for scanning and displaying available Bluetooth LE devices.*

<br>

### 0) Define Variables

```java
private LeDeviceListAdapter mLeDeviceListAdapter;
private BluetoothAdapter mBluetoothAdapter;
private boolean mScanning;
private Handler mHandler;

private static final int REQUEST_ENABLE_BT = 1;
// Stops scanning after 10 seconds.
private static final long SCAN_PERIOD = 10000;
```

<br>

### 1) Realize Adapter

```java
private class LeDeviceListAdapter extends BaseAdapter {
  private ArrayList<BluetoothDevice> mLeDevices;
  private LayoutInflater mInflater;
  
  public LeDeviceListAdapter() {
    super();
    mLeDevice = new ArrayList<BluetoothDevice>();
    mInflater = DeviceScanActivity.this.getLayoutInflater();
  }
  
  public void addDevice(BluetoothDevice device) {
    if(!mLeDevices.contains(device)) {
      mLeDevices.add(device);
    }
  }
  
  public BluetoothDevice getDebvice(int position) { return mLeDevices.get(position); }
  public void clear() { mLeDevices.clear(); }
  
  @Override
  public int getCount() {
    return mLeDevices.size();
  }

  @Override
  public Object getItem(int i) {
    return mLeDevices.get(i);
  }

  @Override
  public long getItemId(int i) {
    return i;
  }

  @Override
  public View getView(int i, View view, ViewGroup viewGroup) {
    ViewHolder viewHolder;
    // General ListView optimization code.
    if (view == null) {
      view = mInflator.inflate(R.layout.listitem_device, null);
      viewHolder = new ViewHolder();
      viewHolder.deviceAddress = (TextView) view.findViewById(R.id.device_address);
      viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
      view.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) view.getTag();
    }

    BluetoothDevice device = mLeDevices.get(i);
    final String deviceName = device.getName();
    if (deviceName != null && deviceName.length() > 0)
      viewHolder.deviceName.setText(deviceName);
    else
      viewHolder.deviceName.setText(R.string.unknown_device);
    viewHolder.deviceAddress.setText(device.getAddress());

    return view;
  }
}
```

