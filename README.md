# 输入校验组件

## 集成方法

```
顶层build.gradle配置：
classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8'


主工程build.gradle配置：
apply plugin: 'com.neenbedankt.android-apt'

dependencies {
    apt 'com.yzd:inputcheck-compiler:1.0.3'
    compile 'com.yzd:inputcheck:1.0.3'
}
```

使用：

```
public class MainActivity extends AppCompatActivity {

    @NotEmpty(R.string.hello_empty)
    EditText helloEt;

    @Match(regex = "^1(3|4|5|7|8)\\d{9}$", message = R.string.dont_match)
    EditText matchEt;

    @NotEmpty(R.string.hello_empty)
    EditText equal1Et;

    @NotEmpty(R.string.hello_empty)
    @Equal(target = "equal1Et", message = R.string.dont_equal)
    EditText equal2Et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helloEt = (EditText) findViewById(R.id.et_hello);
        matchEt = (EditText) findViewById(R.id.et_match);
        equal1Et = (EditText) findViewById(R.id.et_equal1);
        equal2Et = (EditText) findViewById(R.id.et_equal2);
    }

    public void confirm(View view) {
        String errorMessage = InputCheck.check(this);

        if (errorMessage != null) {
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
            return;
        }

        // 业务处理
        Toast.makeText(this, "confirm", Toast.LENGTH_SHORT).show();
    }
}

```