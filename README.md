# 해피쿠폰 외주작업

## TODO

- [ ] serial number 가져오기
- [ ] 영수증 print 기능 MsrDemoActivity에 가져오기
- [ ] 영수증 print내용 꾸미기
- [ ] 쿠폰사용 page http 처리 마무리
- [ ] admin|취소 page UI 작업하기 (디바이스 저장소 사용하기)
- [ ] 환경설정 단말기 환경설정 가져오기(뭐말하는거임 어우 ;)

## Activities

1. MainActivity(홈)
2. MsrDemoActivity(해피쿠용구매)
3. UseHappyCouponActivity(해피쿠폰사용)
4. SaveCashActivity(캐쉬적립)
5. HistoryActivity(거래내역)
6. AdminCancelActivity(Admin|취소)

## build.gradle(:app)

```java
dependencies {
    implementation 'androidx.appcompat:appcompat:1.1.0'
    implementation files ('libs/AndroidAPI.jar')
    implementation files ('libs/emvkernel.jar')
    implementation 'com.squareup.retrofit2:retrofit:2.5.0'
    implementation 'com.google.code.gson:gson:2.8.5'
    implementation 'com.squareup.retrofit2:converter-gson:2.5.0'
    compileOnly files ('libs/device.sdk.classes.jar')
}
```

## AndroidManiffest.xml

```xml
    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
```

