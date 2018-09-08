package com.wxx.test;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.szxb.java8583.core.Iso8583Message;
import com.szxb.java8583.core.Iso8583MessageFactory;
import com.szxb.java8583.module.manager.BusllPosManage;
import com.szxb.java8583.quickstart.SingletonFactory;
import com.szxb.java8583.quickstart.special.SpecialField62;
import com.szxb.jni.libszxb;
import com.union.PosManager;
import com.wxx.test.module.Down;
import com.wxx.test.module.Sign;
import com.wxx.unionpay.R;
import com.wxx.unionpay.UnionPayApp;
import com.wxx.unionpay.db.manager.DBManager;
import com.wxx.unionpay.interfaces.OnCallback;
import com.wxx.unionpay.log.MLog;
import com.wxx.unionpay.socket.RxSocket;
import com.wxx.unionpay.socket.SocketUtil;
import com.wxx.unionpay.socket.ThreadScheduledExecutorUtil;
import com.wxx.unionpay.util.MyToast;
import com.wxx.unionpay.util.Utils;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.wxx.unionpay.util.HexUtil.bytesToHexString;

/**
 * 作者：Tangren on 2018-06-28
 * 包名：com.wxx.test
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */

public class TestActivity extends AppCompatActivity implements OnCallback, View.OnClickListener {
    SocketUtil socket;
    RxSocket rxSocket;
    Button signButton;
    Button queryButton;
    Button downAIDButton;

    Button public_query;
    Button public_down;

    Button rxButton;

    int id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        socket = new SocketUtil();
        rxSocket = new RxSocket();
        socket.setCallback(this);
        signButton = (Button) findViewById(R.id.sign);
        queryButton = (Button) findViewById(R.id.query);
        downAIDButton = (Button) findViewById(R.id.downAID);
        public_query = (Button) findViewById(R.id.public_query);
        public_down = (Button) findViewById(R.id.public_down);
        rxButton = (Button) findViewById(R.id.rx);

        signButton.setOnClickListener(this);
        queryButton.setOnClickListener(this);
        downAIDButton.setOnClickListener(this);
        public_query.setOnClickListener(this);
        public_down.setOnClickListener(this);
        rxButton.setOnClickListener(this);

        AssetManager ass = getApplication().getAssets();
        libszxb.ymodemUpdate(ass, "Q6_K21_171011110309_anjian.bin");


        PosManager manager = new PosManager();
        BusllPosManage.init(manager);

        ThreadScheduledExecutorUtil.getInstance().getService().scheduleAtFixedRate(new LoopThread(rxSocket), 2000, 2000, TimeUnit.MILLISECONDS);
    }


    @Override
    public void onCallBack(byte[] data) {
        Iso8583MessageFactory factory = SingletonFactory.forQuickStart();
        factory.setSpecialFieldHandle(62, new SpecialField62());
        Iso8583Message message0810 = factory.parse(data);


        MLog.d("onCallBack(TestActivity.java:90)原始数据:" + bytesToHexString(data));
        MLog.d("onCallBack(TestActivity.java:99)解析后的数据:" + message0810.toFormatString());

        if (id == R.id.sign) {
            //签到返回
            String batchNum = message0810.getValue(60).getValue().substring(2, 8);
            UnionPayApp.getPosManager().setBatchNum(batchNum);
            Sign.getInstance().setMacKey(message0810.getValue(62).getValue());
        } else if (id == R.id.query) {
            Down.getInstance().setParmaInfo(message0810.getValue(62).getValue());
        } else if (id == R.id.downAID) {
            DBManager.save_ic_params(message0810.getValue(62).getValue());
        } else if (id == R.id.public_query) {
            Down.getInstance().setQueryIndex(message0810.getValue(62).getValue());
        } else if (id == R.id.public_down) {
            DBManager.save_ic_public_key(message0810.getValue(62).getValue());
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign:
                id = R.id.sign;
                socket.setData(Sign.getInstance().message().getBytes());
                socket.exe();
                break;
            case R.id.query:
                id = R.id.query;
                socket.setData(Down.getInstance().message().getBytes());
                socket.exe();
                break;
            case R.id.downAID:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        id = R.id.downAID;
                        String adiList = UnionPayApp.getPosManager().aidIndexList();
                        MLog.d("onClick(TestActivity.java:91)" + adiList);
                        String macs[] = adiList.split(",");
                        for (String mac : macs) {
                            socket.setData(Down.getInstance().messageAID(mac).getBytes());
                            socket.exe();
                            SystemClock.sleep(2000);
                        }
                        MLog.d("run(TestActivity.java:102)AID下载结束");
                        id = 0;
                        socket.setData(Down.getInstance().messageAIDEnd().getBytes());
                        socket.exe();

                    }
                }).start();

                break;
            case R.id.public_query:
                id = R.id.public_query;
                socket.setData(Down.getInstance().messagePublicQuery().getBytes());
                socket.exe();
                break;
            case R.id.public_down:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        id = R.id.public_down;
                        String pubIndex = UnionPayApp.getPosManager().publicIndexList();
                        MLog.d("onClick(TestActivity.java:91)" + pubIndex);
                        String publics[] = pubIndex.split(",");
                        for (String pub : publics) {
                            socket.setData(Down.getInstance().messageDownPublic(pub).getBytes());
                            socket.exe();
                            SystemClock.sleep(2000);
                        }
                        MLog.d("run(TestActivity.java:102)公钥下载结束");
                        id = 0;
                        socket.setData(Down.getInstance().messageDownPublicEnd().getBytes());
                        socket.exe();
                    }
                }).start();
                break;
            default:
//
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        rxSocket.exeSSL(ExeType.SIGN,Sign.getInstance().message().getBytes());
                    }
                }).start();

//                exe();

                break;
        }
    }


    int aidCnt = 0;
    int pubkeyCnt = 0;

    private void exe() {
        Observable.create(new Observable.OnSubscribe<byte[]>() {
            @Override
            public void call(Subscriber<? super byte[]> subscriber) {
                //1.签到
                byte[] exe = rxSocket.exe(Sign.getInstance().message().getBytes());
                if (exe == null) {
                    MyToast.showToast(getApplicationContext(), "签到失败");
                } else {
                    Iso8583MessageFactory factory = SingletonFactory.forQuickStart();
                    factory.setSpecialFieldHandle(62, new SpecialField62());
                    Iso8583Message message0810 = factory.parse(exe);
                    Log.d("TestActivity",
                            "call(TestActivity.java:199)签到:\n" + message0810.toFormatString());
                    String batchNum = message0810.getValue(60).getValue().substring(2, 8);
                    UnionPayApp.getPosManager().setBatchNum(batchNum);
                    Sign.getInstance().setMacKey(message0810.getValue(62).getValue());
                    MyToast.showToast(getApplicationContext(), "签到成功,开始查询参数");
                }
                if (Utils.isUpdateParams()) {
                    subscriber.onNext(exe);
                } else {
                    MyToast.showToast(getApplicationContext(), "参数无需更新");
                    subscriber.isUnsubscribed();
                }
            }
        }).flatMap(new Func1<byte[], Observable<byte[]>>() {
            @Override
            public Observable<byte[]> call(byte[] bytes) {
                //2.查询需要下载的参数
                byte[] exe = rxSocket.exe(Down.getInstance().message().getBytes());
                return Observable.just(exe);
            }
        }).flatMap(new Func1<byte[], Observable<String>>() {
            @Override
            public Observable<String> call(byte[] bytes) {
                if (bytes == null) {
                    MyToast.showToast(getApplicationContext(), "查询参数失败");
                } else {
                    MyToast.showToast(getApplicationContext(), "查询参数成功,开始下载参数");
                    Iso8583MessageFactory factory = SingletonFactory.forQuickStart();
                    factory.setSpecialFieldHandle(62, new SpecialField62());
                    Iso8583Message message0810 = factory.parse(bytes);
                    Log.d("TestActivity",
                            "call(TestActivity.java:219)参数查询：\n" + message0810.toFormatString());
                    Down.getInstance().setParmaInfo(message0810.getValue(62).getValue());
                }
                String adiList = UnionPayApp.getPosManager().aidIndexList();
                String macs[] = adiList.split(",");
                aidCnt = macs.length;
                return Observable.from(macs);
            }
        }).flatMap(new Func1<String, Observable<byte[]>>() {
            @Override
            public Observable<byte[]> call(String s) {
                //3.依次下载参数
                MyToast.showToast(getApplicationContext(), "下载参数中");
                byte[] exe = rxSocket.exe(Down.getInstance().messageAID(s).getBytes());
                return Observable.just(exe);
            }
        }).flatMap(new Func1<byte[], Observable<Integer>>() {
            @Override
            public Observable<Integer> call(byte[] bytes) {
                Iso8583MessageFactory factory = SingletonFactory.forQuickStart();
                factory.setSpecialFieldHandle(62, new SpecialField62());
                Iso8583Message message0810 = factory.parse(bytes);

                DBManager.save_ic_params(message0810.getValue(62).getValue());
                Log.d("TestActivity",
                        "call(TestActivity.java:242)aid参数:\n" + message0810.toFormatString());
                MyToast.showToast(getApplicationContext(), "参数下载中\n剩余" + aidCnt + "个");
                aidCnt -= 1;
                return Observable.just(aidCnt);
            }
        }).filter(new Func1<Integer, Boolean>() {
            @Override
            public Boolean call(Integer integer) {
                return integer <= 0;
            }
        }).flatMap(new Func1<Integer, Observable<byte[]>>() {
            @Override
            public Observable<byte[]> call(Integer integer) {
                //4.下载参数结束
                byte[] exe = rxSocket.exe(Down.getInstance().messageAIDEnd().getBytes());
                return Observable.just(exe);
            }
        }).flatMap(new Func1<byte[], Observable<?>>() {
            @Override
            public Observable<?> call(byte[] bytes) {
                MyToast.showToast(getApplicationContext(), "参数下载结束");
                Iso8583MessageFactory factory = SingletonFactory.forQuickStart();
                factory.setSpecialFieldHandle(62, new SpecialField62());
                Iso8583Message message0810 = factory.parse(bytes);
                Log.d("TestActivity",
                        "call(TestActivity.java:263)aid参数更新结束:\n" + message0810.toFormatString());
                return Observable.just(null);
            }
        }).flatMap(new Func1<Object, Observable<byte[]>>() {
            @Override
            public Observable<byte[]> call(Object o) {
                //5.查询需要下载的公钥
                byte[] exe = rxSocket.exe(Down.getInstance().messagePublicQuery().getBytes());
                return Observable.just(exe);
            }
        }).flatMap(new Func1<byte[], Observable<?>>() {
            @Override
            public Observable<?> call(byte[] bytes) {
                Iso8583MessageFactory factory = SingletonFactory.forQuickStart();
                factory.setSpecialFieldHandle(62, new SpecialField62());
                Iso8583Message message0810 = factory.parse(bytes);
                Down.getInstance().setQueryIndex(message0810.getValue(62).getValue());
                MyToast.showToast(getApplicationContext(), "查询公钥中");
                Log.d("TestActivity",
                        "call(TestActivity.java:280)查询公钥:\n" + message0810.toFormatString());
                return Observable.just(null);
            }
        }).flatMap(new Func1<Object, Observable<String>>() {
            @Override
            public Observable<String> call(Object o) {
                String pubIndex = UnionPayApp.getPosManager().publicIndexList();
                String publics[] = pubIndex.split(",");
                pubkeyCnt = publics.length;
                Log.d("TestActivity",
                        "call(TestActivity.java:293)pubIndex=" + pubIndex);
                return Observable.from(publics);
            }
        }).flatMap(new Func1<String, Observable<byte[]>>() {
            @Override
            public Observable<byte[]> call(String s) {
                // 6.依次下载公钥
                MyToast.showToast(getApplicationContext(), "下载公钥中");
                byte[] exe = rxSocket.exe(Down.getInstance().messageDownPublic(s).getBytes());
                return Observable.just(exe);
            }
        }).flatMap(new Func1<byte[], Observable<Integer>>() {
            @Override
            public Observable<Integer> call(byte[] bytes) {
                Iso8583MessageFactory factory = SingletonFactory.forQuickStart();
                factory.setSpecialFieldHandle(62, new SpecialField62());
                Iso8583Message message0810 = factory.parse(bytes);
                DBManager.save_ic_public_key(message0810.getValue(62).getValue());
                MyToast.showToast(getApplicationContext(), "公钥\n剩余" + pubkeyCnt + "个");
                Log.d("TestActivity",
                        "call(TestActivity.java:310)下载第" + pubkeyCnt + "个公钥:\n" + message0810.toFormatString());
                pubkeyCnt -= 1;
                return Observable.just(pubkeyCnt);
            }
        }).filter(new Func1<Integer, Boolean>() {
            @Override
            public Boolean call(Integer integer) {
                return integer <= 0;
            }
        }).flatMap(new Func1<Integer, Observable<byte[]>>() {
            @Override
            public Observable<byte[]> call(Integer integer) {
                //7.下载公钥结束
                byte[] exe = rxSocket.exe(Down.getInstance().messageDownPublicEnd().getBytes());
                return Observable.just(exe);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<byte[]>() {
                    @Override
                    public void call(byte[] bytes) {
                        UnionPayApp.getPosManager().setCurrentUpdateTime(System.currentTimeMillis() / 1000);
                        Iso8583MessageFactory factory = SingletonFactory.forQuickStart();
                        factory.setSpecialFieldHandle(62, new SpecialField62());
                        Iso8583Message message0810 = factory.parse(bytes);
                        MyToast.showToast(getApplicationContext(), "公钥下载结束");
                        Log.d("TestActivity",
                                "call(TestActivity.java:240)下载公钥结束:\n" + message0810.toFormatString());

                        Log.d("TestActivity",
                                "call(TestActivity.java:345)参数初始化结束");
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.d("TestActivity",
                                "call(TestActivity.java:236)" + throwable.toString());
                    }
                });
    }

}
