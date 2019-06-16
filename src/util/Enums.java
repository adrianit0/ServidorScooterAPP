/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author Adrián
 */

public class Enums {
    public enum TipoTareaEnum {
        LLEVAR_SCOOTER_TALLER   (1, TipoTecnico.TECNICO_RECOGIDA),
        DEVOLVER_SCOOTER_CALLE  (2, TipoTecnico.TECNICO_RECOGIDA),
        CAMBIAR_BATERIAS        (3, TipoTecnico.TECNICO_MANTENIMIENTO),
        APARCAR_BIEN            (4, TipoTecnico.TECNICO_MANTENIMIENTO),
        COMPROBAR_FUNCIONA      (5, TipoTecnico.TECNICO_MECANICO),
        REPARAR_SCOOTER         (6, TipoTecnico.TECNICO_MECANICO),
        ESTIMAR_GASTOS          (7, TipoTecnico.TECNICO_MECANICO);

        private int id;
        private TipoTecnico tipoTecnico;

        TipoTareaEnum (int id, TipoTecnico tecnico) {
            this.id = id;
            this.tipoTecnico = tecnico;
        }

        public int getId(){
            return id;
        }

        public TipoTecnico getTipoTecnico() {
            return tipoTecnico;
        }
        
        public static TipoTareaEnum getById (int id) {
            TipoTareaEnum[] tipos = TipoTareaEnum.values();
            for (TipoTareaEnum t : tipos) {
                if (t.getId()==id)
                    return t;
            }
            return null;
        }
    }

    public enum TipoTecnico {
        TECNICO_RECOGIDA,
        TECNICO_MECANICO,
        TECNICO_MANTENIMIENTO
    }
}
