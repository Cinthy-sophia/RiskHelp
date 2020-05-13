package com.cinthyasophia.riskhelp;

import com.cinthyasophia.riskhelp.modelos.Alerta;

public interface IAlertaListener {
   void onAlertaClicked(Alerta a, String direccion);
}
