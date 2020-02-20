import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Un objeto de esta clase mantiene una 
 * colección map que asocia  categorías (las claves) con
 * la lista (una colección ArrayList) de cursos que pertenecen a esa categoría 
 * Por ej. una entrada del map asocia la categoría 'BASES DE DATOS"' con
 * una lista de cursos de esa categoría
 * 
 * Las claves en el map se recuperan en orden alfabético y 
 * se guardan siempre en mayúsculas
 *
 * @author Sergio Garrués Aizcorbe
 */
public class PlataformaCursos
{

    private final String ESPACIO = " ";
    private final String SEPARADOR = ":";
    private TreeMap<String, ArrayList<Curso>> plataforma;

    /**
     * Constructor  
     */
    public PlataformaCursos() {

        plataforma = new TreeMap<>();

    }

    /**
     * añadir un nuevo curso al map en la categoría indicada
     * Si ya existe la categoría se añade en ella el nuevo curso
     * (al final de la lista)
     * En caso contrario se creará una nueva entrada en el map con
     * la nueva categoría y el curso que hay en ella
     * Las claves siempre se añaden en mayúsculas  
     *  
     */
    public void addCurso(String categoria, Curso curso) {
         ArrayList<Curso> array = new ArrayList<>();
        
         if(!plataforma.containsKey(categoria.toUpperCase())){ 
             array.add(curso);
             plataforma.put(categoria.toUpperCase(), array);
          }
         else{
             plataforma.get(categoria.toUpperCase()).add(curso);
          }

    }

    /**
     *  Devuelve la cantidad de cursos en la categoría indicada
     *  Si no existe la categoría devuelve -1
     *
     */
    public int totalCursosEn(String categoria) {
        int total = -1;
        
        if(plataforma.containsKey(categoria.toUpperCase()))
        {
         total = plataforma.get(categoria.toUpperCase()).size();   
        }
     
        return total;
    }

    /**
     * Representación textual de la plataforma (el map), cada categoría
     * junto con el nº total de cursos que hay en ella y a continuación
     * la relación de cursos en esa categoría (ver resultados de ejecución)
     * 
     * De forma eficiente ya que habrá muchas concatenaciones
     * 
     * Usar el conjunto de entradas y un iterador
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Set<String> keys = plataforma.keySet();
        
        
        for(String clave : keys){
            sb.append(clave + " (" + totalCursosEn(clave) + ")\n");
            
            for(Curso curso : plataforma.get(clave.toUpperCase()))
            {
            sb.append(curso.toString() + "\n\n");
          }
        }
       
         
        return sb.toString();

    }

    /**
     * Mostrar la plataforma
     */
    public void escribir() {

        System.out.println(this.toString());
    }

    /**
     *  Lee de un fichero de texto la información de los cursos
     *  En cada línea del fichero se guarda la información de un curso
     *  con el formato "categoria:nombre:fecha publicacion:nivel"
     *  
     */
    public void leerDeFichero() {

        Scanner sc = new Scanner(
                        this.getClass().getResourceAsStream("/cursos.csv"));
        while (sc.hasNextLine())  {
            String lineaCurso = sc.nextLine().trim();
            int p = lineaCurso.indexOf(SEPARADOR);
            String categoria = lineaCurso.substring(0, p).trim();
            Curso curso = obtenerCurso(lineaCurso.substring(p + 1));
            this.addCurso(categoria, curso);
        }

    }

    /**
     *  Dado un String con los datos de un curso
     *  obtiene y devuelve un objeto Curso
     *
     *  Ej. a partir de  "sql essential training: 3/12/2019 : principiante " 
     *  obtiene el objeto Curso correspondiente
     *  
     *  Asumimos todos los valores correctos aunque puede haber 
     *  espacios antes y después de cada dato
     */
    public Curso obtenerCurso(String lineaCurso) {
        String[] str = lineaCurso.trim().split(SEPARADOR);
        
        LocalDate localDate1 = LocalDate.parse(str[1].trim(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        Curso curso = new Curso(str[0].trim(), localDate1, Nivel.valueOf(str[2].toUpperCase().trim()));
         
        return curso;

    }

    /**
     * devuelve un nuevo conjunto con los nombres de todas las categorías  
     *  
     */
    public TreeSet<String> obtenerCategorias() {
        TreeSet<String> tree = new TreeSet<>();
        Set<String> claves = plataforma.keySet();
        
        for(String key : claves)
        {
            tree.add(key);
        }
        
        return tree;

    }

    /**
     * borra de la plataforma los cursos de la categoría y nivel indicados
     * Se devuelve un conjunto (importa el orden) con los nombres de los cursos borrados 
     * 
     * Asumimos que existe la categoría
     *  
     */

    public   TreeSet<String>   borrarCursosDe(String categoria, Nivel nivel) {
        TreeSet<String> tree = new TreeSet<>();
        Set<String> claves = plataforma.keySet();
        
        for(String clave : claves){
            if(clave.equalsIgnoreCase(categoria))
            {
            
            for(Curso curso : plataforma.get(clave.toUpperCase()))
            {
            
            
                tree.add(curso.getNombre()); 
                plataforma.get(clave).remove(curso);
            }
          }
          
        }
        
        
                
        for(String clave : claves){
            
            
            for(Curso curso : plataforma.get(clave.toUpperCase()))
            {
            if(curso.getNivel().compareTo(nivel) > 0)
            {   
                tree.add(curso.getNombre());
                plataforma.get(clave).remove(curso);
            }
          }
        }
          
        return tree;

    }

    /**
      *   Devuelve el nombre del curso más antiguo en la
      *   plataforma (el primero publicado)
      */

    public String cursoMasAntiguo() {
        String antiguo = "";
         LocalDate fecha = LocalDate.now();
        
        Set<String> keys = plataforma.keySet();
                
        for(String clave : keys){
            
            
            for(Curso curso : plataforma.get(clave.toUpperCase()))
            {
            if(fecha.compareTo(curso.getFecha()) > 0)
            {
                fecha = curso.getFecha();
                antiguo = curso.getNombre();
            }
          }
        }

        return antiguo;
    }

    /**
     *  
     */
    public static void main(String[] args) {

        PlataformaCursos plataforma = new PlataformaCursos();
        plataforma.leerDeFichero();
        plataforma.escribir();

        System.out.println(
                        "Curso más antiguo: " + plataforma.cursoMasAntiguo()
                                        + "\n");

        String categoria = "bases de datos";
        Nivel nivel = Nivel.AVANZADO;
        System.out.println("------------------");
        System.out.println(
                        "Borrando cursos de " + categoria.toUpperCase()
                                        + " y nivel "
                                        + nivel);
        TreeSet<String> borrados = plataforma.borrarCursosDe(categoria, nivel);

        System.out.println("Borrados " + " = " + borrados.toString() + "\n");
        categoria = "cms";
        nivel = Nivel.INTERMEDIO;
        System.out.println(
                        "Borrando cursos de " + categoria.toUpperCase()
                                        + " y nivel "
                                        + nivel);
        borrados = plataforma.borrarCursosDe(categoria, nivel);
        System.out.println("Borrados " + " = " + borrados.toString() + "\n");
        System.out.println("------------------\n");
        System.out.println(
                        "Después de borrar ....");
        plataforma.escribir();

    }
}
