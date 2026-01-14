package com.tierranativa.aplicacion.tierra.nativa.configuration;

import com.tierranativa.aplicacion.tierra.nativa.entity.*;
import com.tierranativa.aplicacion.tierra.nativa.repository.CategoryRepository;
import com.tierranativa.aplicacion.tierra.nativa.repository.CharacteristicRepository;
import com.tierranativa.aplicacion.tierra.nativa.repository.PackageTravelRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
public class DataInitializer implements CommandLineRunner {

    private final PackageTravelRepository packageTravelRepository;
    private final CategoryRepository categoryRepository;
    private final CharacteristicRepository characteristicRepository;

    public DataInitializer(PackageTravelRepository packageTravelRepository, CategoryRepository categoryRepository, CharacteristicRepository characteristicRepository) {
        this.packageTravelRepository = packageTravelRepository;
        this.categoryRepository = categoryRepository;
        this.characteristicRepository = characteristicRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (packageTravelRepository.count() == 0) {
            System.out.println("Cargando datos iniciales de paquetes de viaje...");

            List<PackageTravel> packages = createTravelPackages();
            packageTravelRepository.saveAll(packages);

            System.out.println("Datos iniciales cargados. Total de paquetes: " + packageTravelRepository.count());
        } else {
            System.out.println("La base de datos ya contiene paquetes. Omitiendo la carga inicial.");
        }
    }

    private List<PackageTravel> createTravelPackages() {

        Category geoPaisajes = Category.builder()
                .title("GEOPAISAJES")
                .description("Viajes enfocados en formaciones geológicas, glaciares y paisajes naturales únicos.")
                .imageUrl("https://placehold.co/600x400/0077c2/FFFFFF?text=GeoPaisajes")
                .build();

        Category litoral = Category.builder()
                .title("LITORAL")
                .description("Experiencias en costas, playas, y ecosistemas marinos. Ideal para avistamiento de fauna.")
                .imageUrl("https://placehold.co/600x400/00bcd4/FFFFFF?text=Litoral+Costa")
                .build();

        Category aventura = Category.builder()
                .title("AVENTURA")
                .description("Actividades de alto impacto y adrenalina como deportes extremos, escalada o rafting.")
                .imageUrl("https://placehold.co/600x400/ff9800/FFFFFF?text=Aventura+Extrema")
                .build();

        Category ecoturismo = Category.builder()
                .title("ECOTURISMO")
                .description("Exploración y conservación de áreas naturales, priorizando la sostenibilidad y la educación ambiental.")
                .imageUrl("https://placehold.co/600x400/4caf50/FFFFFF?text=Ecoturismo+Verde")
                .build();

        Category relajacion = Category.builder()
                .title("RELAJACION")
                .description("Destinos y actividades centradas en el bienestar, spa, termas y descanso profundo.")
                .imageUrl("https://placehold.co/600x400/f06292/FFFFFF?text=Relax+Spa")
                .build();

        List<Category> allCategories = Arrays.asList(geoPaisajes, litoral, aventura, ecoturismo,relajacion);
        categoryRepository.saveAll(allCategories);

        PackageCharacteristics wifi = PackageCharacteristics.builder().title("Wi-Fi").icon("wifi").build();
        PackageCharacteristics food = PackageCharacteristics.builder().title("Comida incluida").icon("utensils").build();
        PackageCharacteristics guides = PackageCharacteristics.builder().title("Guías").icon("users").build();
        PackageCharacteristics insurance = PackageCharacteristics.builder().title("Seguro de viaje").icon("shield-check").build();
        PackageCharacteristics equipment = PackageCharacteristics.builder().title("Equipo").icon("mountain").build();
        PackageCharacteristics photography = PackageCharacteristics.builder().title("Fotos").icon("camera").build();
        PackageCharacteristics transport = PackageCharacteristics.builder().title("Traslados").icon("bus").build();
        PackageCharacteristics medical = PackageCharacteristics.builder().title("Emergencias").icon("first-aid").build();
        PackageCharacteristics snacks = PackageCharacteristics.builder().title("Snacks e Hidratación").icon("apple").build();
        PackageCharacteristics maps = PackageCharacteristics.builder().title("Mapas y GPS").icon("map").build();
        PackageCharacteristics localFlights = PackageCharacteristics.builder().title("Vuelos Internos").icon("plane").build();
        PackageCharacteristics entryFees = PackageCharacteristics.builder().title("Entradas Incluidas").icon("ticket").build();
        PackageCharacteristics accommodation = PackageCharacteristics.builder().title("Alojamiento Incluido").icon("hotel").build();
        PackageCharacteristics luxuryHotel = PackageCharacteristics.builder().title("Hotel 5 Estrellas").icon("star").build();
        PackageCharacteristics mountainRefuge = PackageCharacteristics.builder().title("Refugio de Montaña").icon("mountain-snow").build();

        characteristicRepository.saveAll(Arrays.asList(
                snacks, wifi, food, guides, insurance, equipment, photography, transport, medical,
                maps, localFlights, entryFees, accommodation, luxuryHotel,
                mountainRefuge
        ));

        String act1 = "Día 1: Llegada a El Calafate (FTE), traslado al hotel. Día 2: Excursión a las Pasarelas del Glaciar Perito Moreno y Safari Náutico. Día 3: Día de aventura con Minitrekking (opcional) o navegación Big Ice. Día 4: Mañana libre y regreso.";
        String food1 = "Incluye todas las comidas y la hidratación necesaria para la duración del viaje, el plan nutricional está diseñado para variar según la aventura, garantizamos que siempre tendrás el sustento adecuado para disfrutar plenamente de tu viaje.";
        PackageItineraryDetail detail1 = PackageItineraryDetail.builder()
                .duration("4 Días / 3 Noches")
                .lodgingType("Hotel 4 estrellas en El Calafate.")
                .transferType("Vuelo a FTE. Traslados aeropuerto-hotel. Excursiones en bus de turismo.")
                .dailyActivitiesDescription(act1)
                .foodAndHydrationNotes(food1)
                .generalRecommendations("El clima en el Parque Nacional Los Glaciares es frío, ventoso e impredecible, incluso en verano.\n" +
                        "\n" +
                        "Vístete con múltiples capas de ropa, es esencial el uso de chaqueta cortavientos e Impermeable, calzado cómodo y cerrado, así como guantes, gorro y lentes de sol debido a que la nieve y el hielo reflejan intensamente la luz solar, lo que puede dañar tu vista. Y recuerda, la señal de teléfono móvil es limitada o inexistente dentro del Parque, especialmente cerca del glaciar, tomar sus previsiones.")
                .build();
        PackageTravel paquete1 = new PackageTravel();
        paquete1.setName("Glaciar Perito Moreno: Hielo Milenario");
        paquete1.setShortDescription("Un viaje inolvidable para presenciar la majestuosidad del Glaciar Perito Moreno y sus imponentes desprendimientos de hielo en el Parque Nacional Los Glaciares.");
        paquete1.setItineraryDetail(detail1);
        paquete1.setBasePrice(690000.00);
        paquete1.setDestination("El Calafate");
        paquete1.setCategories(Set.of(geoPaisajes));
        paquete1.setCharacteristics(new HashSet<>(Arrays.asList(food, accommodation, transport, guides, snacks, entryFees, medical, photography)));
        paquete1.addImage(PackageImage.builder().url("https://images.pexels.com/photos/17217435/pexels-photo-17217435.jpeg").principal(true).build());
        paquete1.addImage(PackageImage.builder().url("https://images.pexels.com/photos/27180675/pexels-photo-27180675.jpeg").principal(false).build());
        paquete1.addImage(PackageImage.builder().url("https://images.pexels.com/photos/9224586/pexels-photo-9224586.jpeg").principal(false).build());
        paquete1.addImage(PackageImage.builder().url("https://images.pexels.com/photos/26769831/pexels-photo-26769831.jpeg").principal(false).build());
        paquete1.addImage(PackageImage.builder().url("https://images.pexels.com/photos/26893839/pexels-photo-26893839.jpeg").principal(false).build());
        paquete1.addImage(PackageImage.builder().url("https://images.pexels.com/photos/25857467/pexels-photo-25857467.jpeg").principal(false).build());
        paquete1.addImage(PackageImage.builder().url("https://images.pexels.com/photos/15592416/pexels-photo-15592416.jpeg").principal(false).build());


        String act2 = "Día 1: Llegada a El Chaltén, chequeo de equipo y descanso. Día 2: Trekking a Laguna Capri y Base Fitz Roy. Día 3: Trekking a Laguna de los Tres. Día 4: Descenso a la zona de Laguna Torre. Día 5: Trekking a Laguna Torre. Día 6: Senderismo corto y relax. Día 7: Regreso a El Calafate.";
        String food2 = "Incluye todas las comidas y la hidratación necesaria para la duración del viaje, el plan nutricional está diseñado para variar según la aventura, garantizamos que siempre tendrás el sustento adecuado para disfrutar plenamente de tu viaje.";
        PackageItineraryDetail detail2 = PackageItineraryDetail.builder()
                .duration("7 Días / 6 Noches")
                .lodgingType("Hostels/Posadas en El Chaltén y Campamentos organizados/Refugios.")
                .transferType("Vuelo a FTE, Bus a El Chaltén. Recorridos exclusivamente a pie (Trekking).")
                .dailyActivitiesDescription(act2)
                .foodAndHydrationNotes(food2)
                .generalRecommendations("El Fitz Roy es una experiencia de montaña exigente, la planificación es clave para su seguridad y disfrute, vístete con múltiples capas de ropa, priorizando material técnico de secado rápido, es esencial el uso de chaqueta cortavientos e impermeable (capa exterior), el\n" +
                        "\n" +
                        "calzado debe ser botas de trekking de caña media/alta y resistentes al agua, esto es vital para proteger tobillos y mantener los pies secos en senderos con barro o nieve, llevar guantes, gorro (idealmente de lana o polar) y lentes de sol de alta protección UV, la nieve y el hielo reflejan intensamente la luz solar, lo que puede dañar tu vista, la señal de teléfono móvil es limitada o inexistente una vez que sales de El Chaltén y te adentras en los senderos, toma tus previsiones,  y por último, sigue un ritmo constante y consulta a tu guía sobre el nivel de dificultad (especialmente en el tramo final a Laguna de los Tres, que es la parte más dura y empinada).")
                .build();
        PackageTravel paquete2 = new PackageTravel();
        paquete2.setName("Patagonia: Trekking al Fitz Roy");
        paquete2.setShortDescription("Siete días de inmersión total en la Capital Nacional del Trekking. Desafía tus límites con senderos épicos y acampa bajo las siluetas del Fitz Roy y Cerro Torre.");
        paquete2.setItineraryDetail(detail2);
        paquete2.setBasePrice(890000.00);
        paquete2.setDestination("Corrientes");
        paquete2.setCategories(Set.of(aventura));
        paquete2.setCharacteristics(new HashSet<>(Arrays.asList(snacks, food, guides, insurance, equipment, transport, medical, maps, localFlights, mountainRefuge)));
        paquete2.addImage(PackageImage.builder().url("https://images.pexels.com/photos/28359751/pexels-photo-28359751.jpeg").principal(true).build());
        paquete2.addImage(PackageImage.builder().url("https://images.pexels.com/photos/18339547/pexels-photo-18339547.jpeg").principal(false).build());
        paquete2.addImage(PackageImage.builder().url("https://images.pexels.com/photos/13303390/pexels-photo-13303390.jpeg").principal(false).build());
        paquete2.addImage(PackageImage.builder().url("https://images.pexels.com/photos/17445336/pexels-photo-17445336.jpeg").principal(false).build());
        paquete2.addImage(PackageImage.builder().url("https://images.pexels.com/photos/17445329/pexels-photo-17445329.jpeg").principal(false).build());
        paquete2.addImage(PackageImage.builder().url("https://images.pexels.com/photos/17613473/pexels-photo-17613473.jpeg").principal(false).build());
        paquete2.addImage(PackageImage.builder().url("https://images.pexels.com/photos/17300141/pexels-photo-17300141.jpeg").principal(false).build());

        String act3 = "Día 1: Llegada a Puerto Iguazú (IGR), traslado al hotel. Día 2: Parque Nacional Iguazú (Lado Argentino): Circuitos Superior, Inferior y Garganta del Diablo. Día 3: Lado Brasileño y Gran Aventura (paseo en lancha). Día 4: Visita opcional (Selva Iryapú) y regreso.";
        String food3 = "Incluye todas las comidas y la hidratación necesaria para la duración del viaje, el plan nutricional está diseñado para variar según la aventura, garantizamos que siempre tendrás el sustento adecuado para disfrutar plenamente de tu viaje.";
        PackageItineraryDetail detail3 = PackageItineraryDetail.builder()
                .duration("4 Días / 3 Noches")
                .lodgingType("Hotel 4 estrellas en Puerto Iguazú.")
                .transferType("Vuelo a IGR. Buses o taxis para moverse entre el pueblo y el Parque Nacional.")
                .dailyActivitiesDescription(act3)
                .foodAndHydrationNotes(food3)
                .generalRecommendations("El clima es subtropical, caluroso y húmedo todo el año, prepárate para el sol y la alta humedad, de vestimenta usa ropa ligera, clara y de secado rápido, es fundamental llevar protector solar, repelente de insectos y un sombrero o gorra, de calzado, zapatillas cómodas y con buena suela, ideales para caminar por los senderos, evita sandalias abiertas si planeas hacer el circuito inferior o actividades acuáticas, se recomienda tener precaución con la bruma (rocío), coloca tu cámara, celular y documentos en bolsas o fundas impermeables (tipo dry bag),y sobre todo respeta la fauna local, especialmente los coatíes, no los alimentes ni te acerques a ellos, ya que pueden ser agresivos y morder si se sienten amenazados o si buscan comida.")
                .build();
        PackageTravel paquete3 = new PackageTravel();
        paquete3.setName("Cataratas del Iguazú: El Poder del Agua");
        paquete3.setShortDescription("Siente la fuerza de las Cataratas, recorriendo sus tres circuitos y experimentando la biodiversidad de la Selva Paranaense a cada paso.");
        paquete3.setItineraryDetail(detail3);
        paquete3.setBasePrice(480000.00);
        paquete3.setDestination("Misiones");
        paquete3.setCategories(Set.of(litoral));
        paquete3.setCharacteristics(new HashSet<>(Arrays.asList(snacks, wifi, food, guides, photography, transport, localFlights, entryFees, luxuryHotel)));
        paquete3.addImage(PackageImage.builder().url("https://images.pexels.com/photos/1928279/pexels-photo-1928279.jpeg").principal(true).build());
        paquete3.addImage(PackageImage.builder().url("https://images.pexels.com/photos/33688087/pexels-photo-33688087.jpeg").principal(false).build());
        paquete3.addImage(PackageImage.builder().url("https://images.pexels.com/photos/4335018/pexels-photo-4335018.jpeg").principal(false).build());
        paquete3.addImage(PackageImage.builder().url("https://images.pexels.com/photos/10553977/pexels-photo-10553977.jpeg").principal(false).build());
        paquete3.addImage(PackageImage.builder().url("https://images.pexels.com/photos/12045526/pexels-photo-12045526.jpeg").principal(false).build());
        paquete3.addImage(PackageImage.builder().url("https://images.pexels.com/photos/13305789/pexels-photo-13305789.jpeg").principal(false).build());
        paquete3.addImage(PackageImage.builder().url("https://images.pexels.com/photos/1233639/pexels-photo-1233639.jpeg").principal(false).build());


        String act4 = "Día 1: Vuelo a Corrientes/Posadas y traslado al Lodge en Colonia Carlos Pellegrini. Día 2: Navegación en canoa para avistaje de fauna (yacaré, carpinchos). Safari fotográfico nocturno. Día 3: Trekking por senderos dentro de la reserva y avistaje de aves. Día 4: Desayuno y regreso.";
        String food4 = "Incluye todas las comidas y la hidratación necesaria para la duración del viaje, el plan nutricional está diseñado para variar según la aventura, garantizamos que siempre tendrás el sustento adecuado para disfrutar plenamente de tu viaje.";
        PackageItineraryDetail detail4 = PackageItineraryDetail.builder()
                .duration("4 Días / 3 Noches")
                .lodgingType("Lodge o Posada de Ecoturismo en Colonia Carlos Pellegrini.")
                .transferType("Vuelo a CNQ/PSS, seguido de traslado terrestre (4-6h). Recorridos en lancha y vehículo 4x4.")
                .dailyActivitiesDescription(act4)
                .foodAndHydrationNotes(food4)
                .generalRecommendations("lEs una experiencia de inmersión en la naturaleza, con clima subtropical y alta presencia de fauna silvestre, utiliza ropa de colores neutros (verde, beige, caqui) para facilitar la observación de fauna, es indispensable llevar repelente de insectos potente (alto DEET), protector solar y gorra o sombrero de ala ancha, usa calzado cómodo, cerrado, y que pueda mojarse o ensuciarse, la mejor hora para avistar fauna (capibaras, yacarés, ciervos de los pantanos) es al amanecer y al atardecer, mantén la distancia con los animales (especialmente yacarés y víboras) y nunca los alimentes, sigue estrictamente las indicaciones de tu guía local.")
                .build();
        PackageTravel paquete4 = new PackageTravel();
        paquete4.setName("Esteros del Iberá: Corazón de Corrientes");
        paquete4.setShortDescription("La reserva de agua dulce más grande del continente. Navegación en canoa para avistar yacarés, carpinchos y más de 350 especies de aves en total libertad.");
        paquete4.setItineraryDetail(detail4);
        paquete4.setBasePrice(450000.00);
        paquete4.setDestination("Corrientes");
        paquete4.setCategories(Set.of(ecoturismo));
        paquete4.setCharacteristics(new HashSet<>(Arrays.asList(snacks, wifi, food, guides, photography, transport, localFlights,accommodation)));
        paquete4.addImage(PackageImage.builder().url("https://services.meteored.com/img/article/esteros-del-ibera-destino-ideal-para-conectar-con-la-naturaleza-turismo-1734120526464_1280.jpg").principal(true).build());
        paquete4.addImage(PackageImage.builder().url("https://billiken.lat/wp-content/uploads/2022/01/esteros1.jpeg").principal(false).build());
        paquete4.addImage(PackageImage.builder().url("https://images.pexels.com/photos/28939015/pexels-photo-28939015.jpeg").principal(false).build());
        paquete4.addImage(PackageImage.builder().url("https://images.pexels.com/photos/155193/pexels-photo-155193.jpeg").principal(false).build());
        paquete4.addImage(PackageImage.builder().url("https://cloudfront-us-east-1.images.arcpublishing.com/artear/ABFSAMQFGBAXTKBMDVS65ZYRDI.jpeg").principal(false).build());
        paquete4.addImage(PackageImage.builder().url("https://services.meteored.com/img/article/esteros-del-ibera-destino-ideal-para-conectar-con-la-naturaleza-turismo-1734120359086_1280.jpg").principal(false).build());
        paquete4.addImage(PackageImage.builder().url("https://www.clarin.com/2022/08/20/I5u8TKICW_2000x1500__1.jpg").principal(false).build());
        paquete4.addImage(PackageImage.builder().url("https://images.pexels.com/photos/34596000/pexels-photo-34596000.jpeg").principal(false).build());


        String act5 = "Día 1: Llegada a Trelew (REL), traslado a Puerto Madryn. Día 2: Recorrido por la Península Valdés: Avistaje de ballenas (en temporada) y lobos marinos. Día 3: Excursión a Punta Tombo (colonia de pingüinos). Día 4: Visita al Museo Paleontológico y regreso.";
        String food5 = "Incluye todas las comidas y la hidratación necesaria para la duración del viaje, el plan nutricional está diseñado para variar según la aventura, garantizamos que siempre tendrás el sustento adecuado para disfrutar plenamente de tu viaje.";
        PackageItineraryDetail detail5 = PackageItineraryDetail.builder()
                .duration("4 Días / 3 Noches")
                .lodgingType("Hotel 3 estrellas en Puerto Madryn.")
                .transferType("Vuelo a REL. Excursiones en vehículos 4x4 dentro de la Península.")
                .dailyActivitiesDescription(act5)
                .foodAndHydrationNotes(food5)
                .generalRecommendations("Requiere estar en temporada de avistaje (Julio a Diciembre).")
                .build();
        PackageTravel paquete5 = new PackageTravel();
        paquete5.setName("Península Valdés: Gigantes del Mar");
        paquete5.setShortDescription("Encuentro directo con la fauna marina patagónica. Observación de ballenas francas australes (en temporada), elefantes marinos y pingüinos en su ambiente natural..");
        paquete5.setItineraryDetail(detail5);
        paquete5.setBasePrice(550000.00);
        paquete5.setDestination("Chubut");
        paquete5.setCategories(Set.of(ecoturismo));
        paquete5.setCharacteristics(new HashSet<>(Arrays.asList(snacks, food, guides, photography, transport,
                maps, localFlights, entryFees, luxuryHotel)));
        paquete5.addImage(PackageImage.builder().url("https://images.pexels.com/photos/34589749/pexels-photo-34589749.jpeg").principal(true).build());
        paquete5.addImage(PackageImage.builder().url("https://dynamic-media-cdn.tripadvisor.com/media/photo-o/0e/c6/31/66/loberia.jpg?w=900&h=-1&s=1").principal(false).build());
        paquete5.addImage(PackageImage.builder().url("https://cdn.pixabay.com/photo/2020/04/05/10/46/penguin-5005574_1280.jpg").principal(false).build());
        paquete5.addImage(PackageImage.builder().url("https://images.pexels.com/photos/51964/humpback-whale-natural-spectacle-nature-mammal-51964.jpeg").principal(false).build());
        paquete5.addImage(PackageImage.builder().url("https://images.pexels.com/photos/13589248/pexels-photo-13589248.jpeg").principal(false).build());
        paquete5.addImage(PackageImage.builder().url("https://images.pexels.com/photos/17943633/pexels-photo-17943633.jpeg").principal(false).build());
        paquete5.addImage(PackageImage.builder().url("https://images.pexels.com/photos/13581278/pexels-photo-13581278.jpeg").principal(false).build());


        String act6 = "Día 1: Llegada a Stanley. Día 2-3: Vuelo a una isla remota (ej. Saunders Island) para visitar colonias de 5 especies de pingüinos. Día 4: Avistaje de albatros y avifauna antártica. Día 5: Regreso a Stanley y tiempo libre. Día 6: Excursión a las áreas cercanas a la ciudad. Día 7: Regreso.";
        String food6 = "Incluye todas las comidas y la hidratación necesaria para la duración del viaje, el plan nutricional está diseñado para variar según la aventura, garantizamos que siempre tendrás el sustento adecuado para disfrutar plenamente de tu viaje.";
        PackageItineraryDetail detail6 = PackageItineraryDetail.builder()
                .duration("7 Días / 6 Noches")
                .lodgingType("Hoteles sencillos en Stanley y Lodges rurales en las islas periféricas.")
                .transferType("Vuelo a MPN. Vuelos internos FIGAS y vehículos 4x4.")
                .dailyActivitiesDescription(act6)
                .foodAndHydrationNotes(food6)
                .generalRecommendations("Se caracteriza por su clima de estepa, ventoso y con grandes distancias entre puntos de interés, para la vestimenta usa el sistema de capas, incluyendo una chaqueta cortavientos y rompevientos obligatoria, incluso en verano, aunque haga frío, el sol es intenso y el reflejo del mar es alto, por lo que, lleva protector solar, labial con filtro UV y lentes de sol, respeta estrictamente las distancias de avistamiento, se aconseja llevar binoculares para disfrutar de las ballenas, lobos y elefantes marinos sin perturbarlos, recuerda que la principal temporada de ballenas (franca austral) es de junio a diciembre, con el pico en septiembre/octubre, consulta la temporada de cada especie (orcas, pingüinos) antes de tu visita.")
                .build();
        PackageTravel paquete6 = new PackageTravel();
        paquete6.setName(" Islas Malvinas: Santuario Antártico");
        paquete6.setShortDescription("Un viaje exclusivo de ecoturismo en el Atlántico Sur. Camina entre colonias de cinco especies de pingüinos (incluido el Rey) y observa albatros de cerca.");
        paquete6.setItineraryDetail(detail6);
        paquete6.setBasePrice(1900000.00);
        paquete6.setDestination("Isla malvinas");
        paquete6.setCategories(Set.of(ecoturismo));
        paquete6.setCharacteristics(new HashSet<>(Arrays.asList(snacks, food, guides, photography, transport, medical, localFlights, accommodation)));
        paquete6.addImage(PackageImage.builder().url("https://images.pexels.com/photos/7404711/pexels-photo-7404711.jpeg").principal(true).build());
        paquete6.addImage(PackageImage.builder().url("https://images.pexels.com/photos/31307996/pexels-photo-31307996.jpeg").principal(false).build());
        paquete6.addImage(PackageImage.builder().url("https://images.pexels.com/photos/31308018/pexels-photo-31308018.jpeg").principal(false).build());
        paquete6.addImage(PackageImage.builder().url("https://images.pexels.com/photos/14073103/pexels-photo-14073103.jpeg").principal(false).build());
        paquete6.addImage(PackageImage.builder().url("https://images.pexels.com/photos/13653844/pexels-photo-13653844.jpeg").principal(false).build());
        paquete6.addImage(PackageImage.builder().url("https://images.pexels.com/photos/6773715/pexels-photo-6773715.jpeg").principal(false).build());
        paquete6.addImage(PackageImage.builder().url("https://images.pexels.com/photos/31307993/pexels-photo-31307993.jpeg").principal(false).build());


        String act7 = "Día 1: Llegada a Esquel (EQS). Traslado a Trevelin. Tarde dedicada a la visita y fotografía en los campos de tulipanes en flor. Día 2: Mañana en el Parque Nacional Los Alerces. Por la tarde, viaje en La Trochita (tren a vapor). Día 3: Visita al centro de Esquel y regreso.";
        String food7 = "Incluye todas las comidas y la hidratación necesaria para la duración del viaje, el plan nutricional está diseñado para variar según la aventura, garantizamos que siempre tendrás el sustento adecuado para disfrutar plenamente de tu viaje.";
        PackageItineraryDetail detail7 = PackageItineraryDetail.builder()
                .duration("3 Días / 2 Noches")
                .lodgingType("Hotel 3 estrellas en Esquel.")
                .transferType("Vuelo a EQS. Alquiler de coche o excursiones organizadas.")
                .dailyActivitiesDescription(act7)
                .foodAndHydrationNotes(food7)
                .generalRecommendations("La visita a los campos de tulipanes requiere una precisión estacional clave, la floración es un evento muy corto,suele ocurrir en octubre, alcanzando su máximo esplendor a mediados y finales de mes, en vestimenta, utiliza capas de ropa y una chaqueta cortavientos, especialmente si visitas por la mañana o al atardecer, respeta estrictamente los senderos y las áreas delimitadas por los productores, no pises los cultivos ni cortes las flores, la belleza reside en el campo intacto, las mejores horas para visitar y tomar fotografías son las primeras horas de la mañana (luz suave) y la tarde, cerca del atardecer, cuando la luz dorada resalta los colores intensamente.\n" +
                        "\n")
                .build();
        PackageTravel paquete7 = new PackageTravel();
        paquete7.setName("Esquel: La Fiesta de los Tulipanes");
        paquete7.setShortDescription("Vive la primavera en la Patagonia. Una experiencia fotográfica única entre los vibrantes campos de tulipanes en flor de Trevelin, con el marco de la Cordillera.");
        paquete7.setItineraryDetail(detail7);
        paquete7.setBasePrice(350000.00);
        paquete7.setDestination("Chubut");
        paquete7.setCategories(Set.of(geoPaisajes));
        paquete7.setCharacteristics(new HashSet<>(Arrays.asList(snacks, wifi, food, guides, photography, transport,
                localFlights, entryFees, luxuryHotel)));
        paquete7.addImage(PackageImage.builder().url("https://images.pexels.com/photos/13430617/pexels-photo-13430617.jpeg").principal(true).build());
        paquete7.addImage(PackageImage.builder().url("https://images.pexels.com/photos/21558614/pexels-photo-21558614.jpeg").principal(false).build());
        paquete7.addImage(PackageImage.builder().url("https://images.pexels.com/photos/13197004/pexels-photo-13197004.jpeg").principal(false).build());
        paquete7.addImage(PackageImage.builder().url("https://images.pexels.com/photos/5070235/pexels-photo-5070235.jpeg").principal(false).build());
        paquete7.addImage(PackageImage.builder().url("https://images.pexels.com/photos/21705080/pexels-photo-21705080.jpeg").principal(false).build());
        paquete7.addImage(PackageImage.builder().url("https://cdn.pixabay.com/photo/2017/06/15/01/40/chubut-2403830_1280.jpg").principal(false).build());
        paquete7.addImage(PackageImage.builder().url("https://images.pexels.com/photos/18363370/pexels-photo-18363370.jpeg").principal(false).build());

        String act8 = "Día 1: Llegada a Bariloche (BRC). Preparación y compra de provisiones. Día 2: Inicio del Trekking. Ascenso y pernocte en el primer refugio (ej. Refugio Frey). Día 3: Trekking entre refugios. Día 4: Descenso de regreso a la ciudad. Día 5: Descanso y regreso.";
        String food8 = "Incluye todas las comidas y la hidratación necesaria para la duración del viaje, el plan nutricional está diseñado para variar según la aventura, garantizamos que siempre tendrás el sustento adecuado para disfrutar plenamente de tu viaje.";
        PackageItineraryDetail detail8 = PackageItineraryDetail.builder()
                .duration("5 Días / 4 Noches")
                .lodgingType("1 noche en Hotel en Bariloche y 3 noches en Refugios de Montaña.")
                .transferType("Vuelo a BRC. Bus local hasta el inicio de los senderos. Trekking a pie.")
                .dailyActivitiesDescription(act8)
                .foodAndHydrationNotes(food8)
                .generalRecommendations("El circuito de refugios en Bariloche implica alta montaña, requiere excelente estado físico, planificación y equipo adecuado, antes de salir, es mandatorio registrar tu trekking en la página web oficial del Parque Nacional Nahuel Huapi, esto activa el protocolo de rescate en caso de emergencia, en vestimenta usa el sistema de tres capas (térmica, abrigo/polar y chaqueta exterior Gore-Tex o impermeable/cortavientos), en calzado botas de trekking de caña alta ya usadas y probadas, deben ser impermeables y tener buena tracción para roca y nieve,lleva tu propia bolsa de dormir (saco de dormir) y linterna frontal, y recuerda, la conectividad es inexistente entre refugios y en la mayoría de los senderos de montaña, no confíes en el celular para navegación o comunicación.")
                .build();
        PackageTravel paquete8 = new PackageTravel();
        paquete8.setName("Bariloche: Trekking a Refugios");
        paquete8.setShortDescription("Un circuito desafiante de senderismo de varios días que conecta los refugios de montaña más emblemáticos de Bariloche, durmiendo bajo las estrellas.");
        paquete8.setItineraryDetail(detail8);
        paquete8.setBasePrice(620000.00);
        paquete8.setDestination("Rio Negro");
        paquete8.setCategories(Set.of(aventura));
        paquete8.setCharacteristics(new HashSet<>(Arrays.asList(snacks, food, guides, insurance, equipment, photography, transport, medical,
                maps, localFlights, accommodation, mountainRefuge)));
        paquete8.addImage(PackageImage.builder().url("https://images.pexels.com/photos/13353998/pexels-photo-13353998.jpeg").principal(true).build());
        paquete8.addImage(PackageImage.builder().url("https://images.pexels.com/photos/8197821/pexels-photo-8197821.jpeg").principal(false).build());
        paquete8.addImage(PackageImage.builder().url("https://images.pexels.com/photos/18018254/pexels-photo-18018254.jpeg").principal(false).build());
        paquete8.addImage(PackageImage.builder().url("https://images.pexels.com/photos/1537979/pexels-photo-1537979.jpeg").principal(false).build());
        paquete8.addImage(PackageImage.builder().url("https://images.pexels.com/photos/33282750/pexels-photo-33282750.jpeg").principal(false).build());
        paquete8.addImage(PackageImage.builder().url("https://images.pexels.com/photos/33714480/pexels-photo-33714480.jpeg").principal(false).build());
        paquete8.addImage(PackageImage.builder().url("https://images.pexels.com/photos/13581310/pexels-photo-13581310.jpeg").principal(false).build());

        String act9 = "Día 1: Llegada a Ushuaia (USH). Visita al Museo del Presidio. Día 2: Navegación por el Canal Beagle, pasando por el Faro Les Éclaireurs. Día 3: Parque Nacional Tierra del Fuego, incluyendo recorrido en el Tren del Fin del Mundo. Día 4: Subida al Glaciar Martial o Laguna Esmeralda (opcional) y regreso.";
        String food9 = "Incluye todas las comidas y la hidratación necesaria para la duración del viaje, el plan nutricional está diseñado para variar según la aventura, garantizamos que siempre tendrás el sustento adecuado para disfrutar plenamente de tu viaje.";
        PackageItineraryDetail detail9 = PackageItineraryDetail.builder()
                .duration("4 Días / 3 Noches")
                .lodgingType("Hotel 4 estrellas en Ushuaia.")
                .transferType("Vuelo a USH. Traslados aeropuerto-hotel. Excursiones con vehículos de turismo.")
                .dailyActivitiesDescription(act9)
                .foodAndHydrationNotes(food9)
                .generalRecommendations("Ubicada en el extremo sur, presenta un clima subantártico caracterizado por vientos fuertes, bajas temperaturas y cuatro estaciones en un solo día, vístete con capas térmicas de lana o sintéticas, y una chaqueta exterior gruesa, impermeable y cortavientos es obligatoria, utiliza botas o calzado de trekking impermeable con buen agarre, especialmente si visitas el Parque Nacional Tierra del Fuego o realizas caminatas en zonas húmedas o nevadas, usa protector solar y lentes de sol con alta protección, aunque se aceptan tarjetas, es útil llevar algo de efectivo (pesos argentinos) para pequeñas compras o propinas en puertos y refugios.")
                .build();
        PackageTravel paquete9 = new PackageTravel();
        paquete9.setName("Ushuaia: El Último Rincón");
        paquete9.setShortDescription("Descubre el Fin del Mundo. Tren Histórico, navegación por el Canal Beagle y trekking en el Parque Nacional Tierra del Fuego, sintiendo la geografía subantártica.");
        paquete9.setItineraryDetail(detail9);
        paquete9.setBasePrice(750000.00);
        paquete9.setDestination("Tierra del Fuego");
        paquete9.setCategories(Set.of(relajacion));
        paquete9.setCharacteristics(new HashSet<>(Arrays.asList(snacks, food, guides, insurance, equipment, photography, transport, medical,
                maps, localFlights, entryFees, luxuryHotel)));
        paquete9.addImage(PackageImage.builder().url("https://images.pexels.com/photos/3822470/pexels-photo-3822470.jpeg").principal(true).build());
        paquete9.addImage(PackageImage.builder().url("https://images.pexels.com/photos/21424675/pexels-photo-21424675.jpeg").principal(false).build());
        paquete9.addImage(PackageImage.builder().url("https://images.pexels.com/photos/3822475/pexels-photo-3822475.jpeg").principal(false).build());
        paquete9.addImage(PackageImage.builder().url("https://images.pexels.com/photos/13440104/pexels-photo-13440104.jpeg").principal(false).build());
        paquete9.addImage(PackageImage.builder().url("https://images.pexels.com/photos/21533362/pexels-photo-21533362.jpeg").principal(false).build());
        paquete9.addImage(PackageImage.builder().url("https://images.pexels.com/photos/13216184/pexels-photo-13216184.jpeg").principal(false).build());
        paquete9.addImage(PackageImage.builder().url("https://images.pexels.com/photos/32237805/pexels-photo-32237805.jpeg").principal(false).build());


        String act10 = "Día 1: Llegada. Traslado a Purmamarca. Visita al Cerro de los Siete Colores al atardecer. Día 2: Excursión a las Salinas Grandes, experimentando el paisaje blanco. Regreso pasando por la Cuesta del Lipán. Día 3: Recorrido por la Quebrada de Humahuaca (Patrimonio UNESCO): Visita a Tilcara (Pucará) y Uquía. Día 4: Regreso o continuación del viaje.";
        String food10 = "Incluye todas las comidas y la hidratación necesaria para la duración del viaje, el plan nutricional está diseñado para variar según la aventura, garantizamos que siempre tendrás el sustento adecuado para disfrutar plenamente de tu viaje.";
        PackageItineraryDetail detail10 = PackageItineraryDetail.builder()
                .duration("4 Días / 3 Noches")
                .lodgingType("Posada tradicional o hotel boutique en Purmamarca o Salta Capital.")
                .transferType("Vuelo a SLA/JUJ. Se recomienda alquiler de coche o Tour privado/compartido.")
                .dailyActivitiesDescription(act10)
                .foodAndHydrationNotes(food10)
                .generalRecommendations("Esta región se caracteriza por la gran altitud, el clima seco y la intensa radiación solar,\n" +
                        "\n" +
                        "tómate las cosas con calma, camina lento y evita esfuerzos bruscos para prevenir el mal de altura (apunamiento), evita las bebidas alcohólicas el día antes y durante la excursión, es indispensable usar protector solar de alto factor, gorra de ala ancha y lentes de sol de buena calidad, la blancura de la sal en las Salinas multiplica el reflejo solar, usa ropa cómoda y lleva capas de abrigo ligeras, aunque el sol calienta, la temperatura desciende bruscamente al caer la tarde y si hay viento, en Purmamarca, visita el Cerro de los Siete Colores durante la mañana, la luz del sol lo ilumina de frente, revelando mejor sus colores, en las Salinas, usa la luz de la mañana o la tarde para evitar el calor extremo del mediodía y lograr mejores fotos de perspectiva.")
                .build();
        PackageTravel paquete10 = new PackageTravel();
        paquete10.setName("Salinas Grandes y Cerro 7 Colores");
        paquete10.setShortDescription("Un viaje por la Quebrada de Humahuaca (Patrimonio de la Humanidad). Contraste único entre la paleta de colores del Cerro y la inmensidad blanca de las Salinas Grandes.");
        paquete10.setItineraryDetail(detail10);
        paquete10.setBasePrice(420000.00);
        paquete10.setDestination("Jujuy / Salta");
        paquete10.setCategories(Set.of(geoPaisajes));
        paquete10.setCharacteristics(new HashSet<>(Arrays.asList(snacks, food, guides, photography, transport,localFlights, accommodation)));
        paquete10.addImage(PackageImage.builder().url("https://images.pexels.com/photos/33625906/pexels-photo-33625906.jpeg").principal(true).build());
        paquete10.addImage(PackageImage.builder().url("https://images.pexels.com/photos/13555662/pexels-photo-13555662.jpeg").principal(false).build());
        paquete10.addImage(PackageImage.builder().url("https://images.pexels.com/photos/12802851/pexels-photo-12802851.jpeg").principal(false).build());
        paquete10.addImage(PackageImage.builder().url("https://images.pexels.com/photos/4613200/pexels-photo-4613200.jpeg").principal(false).build());
        paquete10.addImage(PackageImage.builder().url("https://images.pexels.com/photos/13430664/pexels-photo-13430664.jpeg").principal(false).build());
        paquete10.addImage(PackageImage.builder().url("https://images.pexels.com/photos/32305986/pexels-photo-32305986.jpeg").principal(false).build());
        paquete10.addImage(PackageImage.builder().url("https://images.pexels.com/photos/6134593/pexels-photo-6134593.jpeg").principal(false).build());

        return Arrays.asList(
                paquete1, paquete2, paquete3, paquete4, paquete5,
                paquete6, paquete7, paquete8, paquete9, paquete10
        );
    }
}
