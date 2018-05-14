package hackaton.blockchain.walkwithmeanalytics;

import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.punchthrough.bean.sdk.Bean;
import com.punchthrough.bean.sdk.BeanListener;
import com.punchthrough.bean.sdk.message.Acceleration;
import com.punchthrough.bean.sdk.message.BeanError;
import com.punchthrough.bean.sdk.message.LedColor;
import com.punchthrough.bean.sdk.message.ScratchBank;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class setup_config extends AppCompatActivity {

    EditText nombre, tiempo, intervalo;
    Button iniciar, stop;
    Bean wwmeDevice;
    TextView console;
    String datas = "";
    CountDownTimer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_config);

        nombre = (EditText)findViewById(R.id.nombre_editText);
        tiempo = (EditText)findViewById(R.id.tiempo_editText);
        intervalo = (EditText)findViewById(R.id.intervalo_editText);
        iniciar = (Button)findViewById(R.id.iniciar_button);
        stop = (Button)findViewById(R.id.stop_button);
        console = (TextView)findViewById(R.id.console_text);
        wwmeDevice = (Bean) getIntent().getExtras().get("connect");

        iniciar.setEnabled(false);
        stop.setEnabled(false);

        nombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(nombre.getText().toString().isEmpty() || tiempo.getText().toString().isEmpty() || intervalo.getText().toString().isEmpty()){
                    iniciar.setEnabled(false);
                }else{
                    iniciar.setEnabled(true);
                }
            }
        });

        tiempo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(nombre.getText().toString().isEmpty() || tiempo.getText().toString().isEmpty() || intervalo.getText().toString().isEmpty()){
                    iniciar.setEnabled(false);
                }else{
                    iniciar.setEnabled(true);
                }
            }
        });

        iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciar.setEnabled(false);
                String nom = nombre.getText().toString();
                int seg = Integer.parseInt(tiempo.getText().toString());
                int interval = Integer.parseInt(intervalo.getText().toString());
                boolean allOk = true;

                //comprobación
                if(nom.isEmpty()){
                    allOk = false;
                    Toast.makeText(getApplicationContext(), "Nombre no puede ser vacio", Toast.LENGTH_LONG).show();
                    nombre.requestFocus();
                }
                if(seg > 5000){
                    allOk = false;
                    Toast.makeText(getApplicationContext(), "Tiempo debe ser superior a 5000 (5 segundos)", Toast.LENGTH_LONG).show();
                    tiempo.requestFocus();
                }

                if(interval < 200){
                    allOk = false;
                    Toast.makeText(getApplicationContext(), "Intervalo debe ser mayor que 200",Toast.LENGTH_LONG).show();
                    intervalo.requestFocus();
                }

                if(allOk){
                    capturaData(nom,seg,interval);
                    stop.setEnabled(true);
                }else{
                    iniciar.setEnabled(true);
                    stop.setEnabled(false);
                }
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.cancel();
                stop.setEnabled(false);
                iniciar.setEnabled(true);
            }
        });
    }


    void capturaData(final String nombre, final int segundos, final int intervalo){

        console.setText("...CARGANDO...");

        datas = "";
        final BeanListener wwmeDeviceListener = new BeanListener() {
            @Override
            public void onConnected() {
                console.setText("...GUARDANDO LECTURA...");
                timer = new CountDownTimer(segundos, intervalo) {
                    public void onTick(long millisUntilFinished) {
                        iniciar.setText(""+millisUntilFinished / 1000);
                        wwmeDevice.setLed(LedColor.create(0, 0, 255));
                        wwmeDevice.setLed(LedColor.create(0,0,0));
                        wwmeDevice.readAcceleration(new com.punchthrough.bean.sdk.message.Callback<Acceleration>() {
                            @Override
                            public void onResult(Acceleration result) {
                                datas = datas + result.x() + "," + result.y() + "," + result.z() + "," + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new java.util.Date()) + "\n";
                            }
                        });

                    }

                    public void onFinish() {
                        console.setText("...GUARDANDO...");
                        guardarData(nombre, datas);
                    }
                };
                timer.start();
            }

            @Override
            public void onConnectionFailed() {

            }

            @Override
            public void onDisconnected() {

            }

            @Override
            public void onSerialMessageReceived(byte[] data) {

            }

            @Override
            public void onScratchValueChanged(ScratchBank bank, byte[] value) {

            }

            @Override
            public void onError(BeanError error) {

            }

            @Override
            public void onReadRemoteRssi(int rssi) {

            }
        };
        wwmeDevice.connect(setup_config.this, wwmeDeviceListener);

    }

    protected void guardarData(String actionName, String datas) {
        File Root = Environment.getExternalStorageDirectory();
        File dir = new File(Root + "/csvFiles");
        if (!dir.exists()) {
            dir.mkdir();
        }
        File file = new File(dir, actionName +"-"+ new SimpleDateFormat("yyyy-MM-ddhh:mm:ss").format(new java.util.Date()) + ".csv");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(datas.getBytes());
            fileOutputStream.close();
            console.setText("CSV GUARDADO");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
