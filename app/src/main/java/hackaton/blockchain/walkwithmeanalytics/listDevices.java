package hackaton.blockchain.walkwithmeanalytics;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.punchthrough.bean.sdk.Bean;
import com.punchthrough.bean.sdk.BeanDiscoveryListener;
import com.punchthrough.bean.sdk.BeanManager;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by m4uro on 12-05-18.
 */

public class listDevices extends ListActivity implements Serializable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ArrayList<Bean> beans = new ArrayList<>();

        final ArrayAdapter<Bean> Dispositivos = new ArrayAdapter(this, android.R.layout.simple_list_item_1);

        BeanDiscoveryListener listener = new BeanDiscoveryListener() {
            @Override
            public void onBeanDiscovered(Bean bean, int rssi) {
                String addr = bean.getDevice().getAddress();
                for(Bean b: beans){
                    if(Dispositivos.getCount() > 0){

                        Bean ultimoRegistro = Dispositivos.getItem(Dispositivos.getCount()-1);
                        if(ultimoRegistro != b){
                            if(b.getDevice().getAddress().equals(addr)){
                                Dispositivos.add(b);
                            }
                        }
                    }else{
                        Dispositivos.add(b);
                    }
                }
                beans.add(bean);
                setListAdapter(Dispositivos);
            }
            @Override
            public void onDiscoveryComplete() {

            }
        };
        BeanManager.getInstance().setScanTimeout(5);  // Timeout in seconds, optional, default is 30 seconds
        BeanManager.getInstance().startDiscovery(listener);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        /*final Intent returnLectura = new Intent(this, setup_config.class);
        Bean wwmesDevice = (Bean) l.getAdapter().getItem(position);
        returnLectura.putExtra("connect", wwmesDevice);
        startActivity(returnLectura);
        MainActivity.Main.finish();*/
    }
}
