package domain;

import java.util.ArrayList;
import java.util.List;

public class CargadorDatos {

    public static List<Perfume> cargarPerfumes(List<Categoria> categorias) {
        List<Perfume> listaPerfumes = new ArrayList<>();

        // Identificadores de Categorías (según el orden de creación)
        Categoria catArabeM = categorias.get(0); // Árabes Masculinos
        Categoria catMasculina = categorias.get(1); // Perfumería Masculina
        Categoria catArabeF = categorias.get(2); // Árabes Femenino
        Categoria catFemenina = categorias.get(3); // Perfumería Femenina

        int id = 1;

        // ==========================================
        // 1. ÁRABES MASCULINOS (53 Perfumes)
        // ==========================================
        // LATTAFA
        listaPerfumes.add(new Perfume(id++, "Asad", "Lattafa", 240000.0, 100, "Pimienta negra, Piña, Tabaco", catArabeM));
        listaPerfumes.add(new Perfume(id++, "Khamrah", "Lattafa", 250000.0, 100, "Canela, Nuez moscada, Praliné", catArabeM));
        listaPerfumes.add(new Perfume(id++, "Khamrah Qahwa", "Lattafa", 270000.0, 100, "Café, Canela, Vainilla", catArabeM));
        listaPerfumes.add(new Perfume(id++, "Sublime (Bade'e Al Oud)", "Lattafa", 230000.0, 100, "Manzana, Rosa, Ciruela", catArabeM));
        listaPerfumes.add(new Perfume(id++, "Amethyst (Bade'e Al Oud)", "Lattafa", 230000.0, 100, "Pimienta rosa, Rosa de Bulgaria", catArabeM));
        listaPerfumes.add(new Perfume(id++, "Oud for Glory (Bade'e Al Oud)", "Lattafa", 230000.0, 100, "Azafrán, Nuez moscada, Oud", catArabeM));
        listaPerfumes.add(new Perfume(id++, "Honor & Glory (Bade'e Al Oud)", "Lattafa", 240000.0, 100, "Piña, Brulee, Canela", catArabeM));
        listaPerfumes.add(new Perfume(id++, "His Confession", "Lattafa", 280000.0, 100, "Canela, Iris, Tonka", catArabeM));
        listaPerfumes.add(new Perfume(id++, "Qaed Al Fursan", "Lattafa", 190000.0, 90, "Piña, Azafrán, Jazmín", catArabeM));
        listaPerfumes.add(new Perfume(id++, "Qaed Al Fursan Unlimited", "Lattafa", 190000.0, 90, "Coco, Cítricos, Ylang-Ylang", catArabeM));
        listaPerfumes.add(new Perfume(id++, "Maahir", "Lattafa", 210000.0, 100, "Azafrán, Rosa, Ámbar", catArabeM));
        listaPerfumes.add(new Perfume(id++, "Maahir Black Edition", "Lattafa", 220000.0, 100, "Pimienta negra, Incienso, Cuero", catArabeM));
        listaPerfumes.add(new Perfume(id++, "Shaheen Gold", "Lattafa", 250000.0, 100, "Piña, Toronja, Lavanda", catArabeM));
        listaPerfumes.add(new Perfume(id++, "Shaheen Silver", "Lattafa", 250000.0, 100, "Casis, Bergamota, Rosa", catArabeM));
        listaPerfumes.add(new Perfume(id++, "Yara", "Lattafa", 210000.0, 100, "Orquídea, Vainilla, Almizcle", catArabeM));
        listaPerfumes.add(new Perfume(id++, "Ejaazi", "Lattafa", 180000.0, 100, "Cítricos, Cardamomo, Cedro", catArabeM));
        listaPerfumes.add(new Perfume(id++, "Ejaazi Intensive Silver", "Lattafa", 180000.0, 100, "Mandaria, Toronja, Nota marina", catArabeM));
        listaPerfumes.add(new Perfume(id++, "Al Noble Ameer", "Lattafa", 210000.0, 100, "Manzana, Pimienta rosa, Romero", catArabeM));

        // MAISON ALHAMBRA
        listaPerfumes.add(new Perfume(id++, "Jorge Di Profumo", "Maison Alhambra", 190000.0, 100, "Notas marinas, Incienso, Patchouli", catArabeM));
        listaPerfumes.add(new Perfume(id++, "Amber & Leather", "Maison Alhambra", 190000.0, 100, "Cuero, Cardamomo, Ámbar", catArabeM));
        listaPerfumes.add(new Perfume(id++, "Tobacco Touch", "Maison Alhambra", 190000.0, 100, "Tabaco, Vainilla, Especias", catArabeM));
        listaPerfumes.add(new Perfume(id++, "Porto Neroli", "Maison Alhambra", 190000.0, 100, "Neroli, Cítricos, Jazmín", catArabeM));
        listaPerfumes.add(new Perfume(id++, "Woody Oud", "Maison Alhambra", 190000.0, 100, "Oud, Palo de Rosa, Cardamomo", catArabeM));
        listaPerfumes.add(new Perfume(id++, "Toscano Leather", "Maison Alhambra", 190000.0, 100, "Cuero, Frambuesa, Azafrán", catArabeM));
        listaPerfumes.add(new Perfume(id++, "Fabulous Life", "Maison Alhambra", 190000.0, 100, "Almendra amarga, Cuero, Orris", catArabeM));
        listaPerfumes.add(new Perfume(id++, "Hercules", "Maison Alhambra", 190000.0, 100, "Canela, Hojas de Tabaco, Incienso", catArabeM));
        listaPerfumes.add(new Perfume(id++, "Kismet For Men", "Maison Alhambra", 200000.0, 100, "Madera, Café, Cacao", catArabeM));
        listaPerfumes.add(new Perfume(id++, "Jean Lowe Noir", "Maison Alhambra", 220000.0, 100, "Oud, Incienso, Cacao", catArabeM));

        // ORIENTICA
        listaPerfumes.add(new Perfume(id++, "Royal Bleu", "Orientica", 420000.0, 80, "Manzana, Lavanda, Pimienta", catArabeM));
        listaPerfumes.add(new Perfume(id++, "Fantasy", "Orientica", 380000.0, 80, "Nuez moscada, Manzanilla, Ámbar", catArabeM));
        listaPerfumes.add(new Perfume(id++, "Amber Rouge", "Orientica", 420000.0, 80, "Jazmín, Azafrán, Ámbar gris", catArabeM));
        listaPerfumes.add(new Perfume(id++, "Amber Noir", "Orientica", 420000.0, 80, "Zanahoria, Elemi, Madera", catArabeM));
        listaPerfumes.add(new Perfume(id++, "Velvet Gold", "Orientica", 450000.0, 80, "Pimienta rosa, Violeta, Vainilla", catArabeM));

        // ARMAF
        listaPerfumes.add(new Perfume(id++, "Odyssey Wild One", "Armaf", 240000.0, 100, "Pimienta, Menta, Bergamota", catArabeM));
        listaPerfumes.add(new Perfume(id++, "Odyssey Mandarin Sky", "Armaf", 250000.0, 100, "Mandarina, Caramelo, Habita Tonka", catArabeM));
        listaPerfumes.add(new Perfume(id++, "Club de Nuit Intense Man", "Armaf", 260000.0, 105, "Limón, Piña, Abedul, Almizcle", catArabeM));
        listaPerfumes.add(new Perfume(id++, "Club de Nuit Iconic", "Armaf", 320000.0, 105, "Toronja, Menta, Jengibre", catArabeM));
        listaPerfumes.add(new Perfume(id++, "Club de Nuit Untold", "Armaf", 350000.0, 105, "Azafrán, Jazmín, Madera de Ámbar", catArabeM));
        listaPerfumes.add(new Perfume(id++, "Club de Nuit Sillage", "Armaf", 280000.0, 105, "Bergamota, Grosellas negras, Violeta", catArabeM));
        listaPerfumes.add(new Perfume(id++, "Club de Nuit Milestone", "Armaf", 280000.0, 105, "Notas marinas, Cítricos, Violeta", catArabeM));
        listaPerfumes.add(new Perfume(id++, "Club de Nuit Urban Elixir", "Armaf", 290000.0, 105, "Pimienta rosa, Lavanda, Vetiver", catArabeM));

        // AL HARAMAIN
        listaPerfumes.add(new Perfume(id++, "L'Aventure", "Al Haramain", 280000.0, 100, "Limón, Bergamota, Elemi", catArabeM));
        listaPerfumes.add(new Perfume(id++, "L'Aventure Knight", "Al Haramain", 290000.0, 100, "Verbona, Té, Violeta", catArabeM));
        listaPerfumes.add(new Perfume(id++, "Amber Oud Gold Edition", "Al Haramain", 380000.0, 120, "Bergamota, Notas verdes, Melón", catArabeM));
        listaPerfumes.add(new Perfume(id++, "Amber Oud Rouge Edition", "Al Haramain", 390000.0, 120, "Azafrán, Madera de Ámbar", catArabeM));
        listaPerfumes.add(new Perfume(id++, "Amber Oud Tobacco Edition", "Al Haramain", 390000.0, 120, "Jengibre, Pimienta, Canela", catArabeM));
        listaPerfumes.add(new Perfume(id++, "Premium Collection", "Al Haramain", 350000.0, 100, "Notas florales, Amaderadas", catArabeM));

        // AFNAN
        listaPerfumes.add(new Perfume(id++, "9 PM Classic", "Afnan", 220000.0, 100, "Manzana, Canela, Lavanda, Vainilla", catArabeM));
        listaPerfumes.add(new Perfume(id++, "9 AM Dive", "Afnan", 220000.0, 100, "Menta, Limón, Manzana verde", catArabeM));
        listaPerfumes.add(new Perfume(id++, "Supremacy Not Only Intense", "Afnan", 290000.0, 100, "Grosella negra, Bergamota, Liquen", catArabeM));
        listaPerfumes.add(new Perfume(id++, "Supremacy In Heaven", "Afnan", 260000.0, 100, "Bergamota, Mandarina, Té verde", catArabeM));
        listaPerfumes.add(new Perfume(id++, "Turathi Blue", "Afnan", 260000.0, 90, "Cítricos, Ámbar, Amaderado", catArabeM));
        listaPerfumes.add(new Perfume(id++, "Historic Olmeda", "Afnan", 250000.0, 100, "Pimienta rosa, Cardamomo, Pomelo", catArabeM));


        // ==========================================
        // 2. PERFUMERÍA MASCULINA (46 Perfumes)
        // ==========================================
        // ILMIN
        listaPerfumes.add(new Perfume(id++, "Il Kakao / Ilmin Parfums", "Ilmin", 620000.0, 30, "Cacao, Vainilla, Especias", catMasculina));
        listaPerfumes.add(new Perfume(id++, "Il Carmine", "Ilmin", 620000.0, 30, "Notas amaderadas, Ámbar", catMasculina));
        listaPerfumes.add(new Perfume(id++, "Il Sesso", "Ilmin", 620000.0, 30, "Cítricos, Almizcle, Cuero", catMasculina));
        listaPerfumes.add(new Perfume(id++, "Il Male", "Ilmin", 620000.0, 30, "Pimienta, Lavanda, Sándalo", catMasculina));
        listaPerfumes.add(new Perfume(id++, "Il Regalo", "Ilmin", 620000.0, 30, "Notas orientales, Tabaco", catMasculina));
        listaPerfumes.add(new Perfume(id++, "Il Pascolo", "Ilmin", 620000.0, 30, "Hierbas frescas, Vetiver", catMasculina));
        listaPerfumes.add(new Perfume(id++, "Il Oro", "Ilmin", 650000.0, 30, "Azafrán, Ámbar, Oud", catMasculina));
        listaPerfumes.add(new Perfume(id++, "Il Rosso", "Ilmin", 620000.0, 30, "Frutos rojos, Madera", catMasculina));
        listaPerfumes.add(new Perfume(id++, "Il Noche", "Ilmin", 620000.0, 30, "Incienso, Cuero, Especias", catMasculina));
        listaPerfumes.add(new Perfume(id++, "Il Cuoio", "Ilmin", 620000.0, 30, "Cuero, Tabaco, Madera", catMasculina));
        listaPerfumes.add(new Perfume(id++, "Il Blanco", "Ilmin", 620000.0, 30, "Notas blancas, Cítricos", catMasculina));
        listaPerfumes.add(new Perfume(id++, "Il Verde", "Ilmin", 620000.0, 30, "Notas verdes, Menta", catMasculina));

        // DEVIER
        listaPerfumes.add(new Perfume(id++, "Black Religion", "Devier", 380000.0, 100, "Incienso, Maderas oscuras", catMasculina));
        listaPerfumes.add(new Perfume(id++, "Spice Mexican", "Devier", 380000.0, 100, "Especias, Vainilla", catMasculina));
        listaPerfumes.add(new Perfume(id++, "Forgive Us", "Devier", 380000.0, 100, "Ámbar, Notas dulces", catMasculina));
        listaPerfumes.add(new Perfume(id++, "Strong as Metal", "Devier", 380000.0, 100, "Notas metálicas, Cítricos", catMasculina));
        listaPerfumes.add(new Perfume(id++, "VIP Carat", "Devier", 390000.0, 100, "Especias finas, Ámbar", catMasculina));
        listaPerfumes.add(new Perfume(id++, "Amber Black", "Devier", 380000.0, 100, "Ámbar, Madera", catMasculina));
        listaPerfumes.add(new Perfume(id++, "Satin Rose", "Devier", 380000.0, 100, "Rosa, Almizcle", catMasculina));
        listaPerfumes.add(new Perfume(id++, "Oud Noir", "Devier", 390000.0, 100, "Oud, Cuero", catMasculina));
        listaPerfumes.add(new Perfume(id++, "Drew", "Devier", 380000.0, 100, "Frutal, Fresco", catMasculina));
        listaPerfumes.add(new Perfume(id++, "Lermo", "Devier", 380000.0, 100, "Cítrico, Amaderado", catMasculina));

        // GIORGIO ARMANI & YSL
        listaPerfumes.add(new Perfume(id++, "Acqua Di Giò Pour Homme", "Giorgio Armani", 520000.0, 100, "Notas marinas, Lima, Bergamota", catMasculina));
        listaPerfumes.add(new Perfume(id++, "Acqua Di Giò Profondo", "Giorgio Armani", 580000.0, 100, "Notas marinas, Romero, Lavanda", catMasculina));
        listaPerfumes.add(new Perfume(id++, "Acqua Di Giò Parfum", "Giorgio Armani", 640000.0, 100, "Bergamota, Incienso, Patchouli", catMasculina));
        listaPerfumes.add(new Perfume(id++, "Acqua Di Giò Absolu", "Giorgio Armani", 590000.0, 100, "Notas marinas, Pomelo, Tonka", catMasculina));
        listaPerfumes.add(new Perfume(id++, "MYSLF Eau de Parfum", "Yves Saint Laurent", 620000.0, 100, "Bergamota, Azahar, Patchouli", catMasculina));

        // JO MILANO PARIS
        listaPerfumes.add(new Perfume(id++, "Game of Spades Ace", "Jo Milano Paris", 420000.0, 100, "Cítricos, Amaderado", catMasculina));
        listaPerfumes.add(new Perfume(id++, "Game of Spades Queen", "Jo Milano Paris", 420000.0, 100, "Floral, Frutal", catMasculina));
        listaPerfumes.add(new Perfume(id++, "Game of Spades King", "Jo Milano Paris", 420000.0, 100, "Notas dulces, Ámbar", catMasculina));
        listaPerfumes.add(new Perfume(id++, "Game of Spades Royale", "Jo Milano Paris", 450000.0, 100, "Madera, Especias", catMasculina));
        listaPerfumes.add(new Perfume(id++, "Game of Spades Wild Card", "Jo Milano Paris", 430000.0, 100, "Fresco, Especiado", catMasculina));

        // CAROLINA HERRERA & VALENTINO
        listaPerfumes.add(new Perfume(id++, "Bad Boy", "Carolina Herrera", 480000.0, 100, "Pimienta negra, Pimienta blanca, Cacao", catMasculina));
        listaPerfumes.add(new Perfume(id++, "Bad Boy Cobalt", "Carolina Herrera", 520000.0, 100, "Pimienta rosa, Ciruela, Trufa", catMasculina));
        listaPerfumes.add(new Perfume(id++, "Born in Roma Uomo", "Valentino", 630000.0, 100, "Hojas de violeta, Sal de mar, Jengibre", catMasculina));
        listaPerfumes.add(new Perfume(id++, "Born in Roma Intense Uomo", "Valentino", 680000.0, 100, "Vainilla, Lavanda, Vetiver", catMasculina));

        // PACO RABANNE & JEAN PAUL GAULTIER
        listaPerfumes.add(new Perfume(id++, "1 Million", "Paco Rabanne", 490000.0, 100, "Mandarina sangrienta, Canela, Cuero", catMasculina));
        listaPerfumes.add(new Perfume(id++, "1 Million Royal / Elixir", "Paco Rabanne", 550000.0, 100, "Manzana, Lavanda, Benzoin", catMasculina));
        listaPerfumes.add(new Perfume(id++, "Le Male Elixir", "Jean Paul Gaultier", 590000.0, 125, "Menta, Lavanda, Benzoin, Miel", catMasculina));
        listaPerfumes.add(new Perfume(id++, "Le Male Le Parfum", "Jean Paul Gaultier", 560000.0, 125, "Cardamomo, Lavanda, Iris, Vainilla", catMasculina));

        // OTRAS MARCAS INTERNACIONALES
        listaPerfumes.add(new Perfume(id++, "L.12.12 Blanc", "Lacoste", 360000.0, 100, "Toronja, Cardamomo, Romera, Tuberosa", catMasculina));
        listaPerfumes.add(new Perfume(id++, "L.12.12 Black / Noir", "Lacoste", 360000.0, 100, "Sandía, Lavanda, Basílico, Chocolate", catMasculina));
        listaPerfumes.add(new Perfume(id++, "Scent of Peace for Him", "BOND No. 9", 1350000.0, 100, "Piña, Baya de enebro, Bergamota", catMasculina));
        listaPerfumes.add(new Perfume(id++, "Aventus", "Creed", 1650000.0, 100, "Piña, Bergamota, Grosella negra, Abedul", catMasculina));
        listaPerfumes.add(new Perfume(id++, "Eros Eau de Parfum / Parfum", "Versace", 480000.0, 100, "Menta, Manzana verde, Limón, Habain Tonka", catMasculina));
        listaPerfumes.add(new Perfume(id++, "Dylan Blue Pour Homme", "Versace", 440000.0, 100, "Notas acuáticas, Bergamota, Toronja, Incienso", catMasculina));


        // ==========================================
        // 3. ÁRABES FEMENINO (55 Perfumes)
        // ==========================================
        // LATTAFA
        listaPerfumes.add(new Perfume(id++, "Yara (Rosa)", "Lattafa", 210000.0, 100, "Orquídea, Heliotropo, Tangerina, Vainilla", catArabeF));
        listaPerfumes.add(new Perfume(id++, "Yara Tous (Amarillo)", "Lattafa", 220000.0, 100, "Mango, Coco, Maracuyá, Jazmín", catArabeF));
        listaPerfumes.add(new Perfume(id++, "Yara Moi (Blanco/Melocotón)", "Lattafa", 220000.0, 100, "Jazmín, Melocotón, Caramelo", catArabeF));
        listaPerfumes.add(new Perfume(id++, "Yara Candy (Verde/Rosa)", "Lattafa", 240000.0, 100, "Grosella negra, Caramelos, Vainilla", catArabeF));
        listaPerfumes.add(new Perfume(id++, "Khamrah", "Lattafa", 250000.0, 100, "Canela, Nuez moscada, Praliné", catArabeF));
        listaPerfumes.add(new Perfume(id++, "Khamrah Qahwa", "Lattafa", 270000.0, 100, "Café, Canela, Vainilla", catArabeF));
        listaPerfumes.add(new Perfume(id++, "Sublime (Bade'e Al Oud)", "Lattafa", 230000.0, 100, "Manzana, Rosa, Ciruela", catArabeF));
        listaPerfumes.add(new Perfume(id++, "Amethyst (Bade'e Al Oud)", "Lattafa", 230000.0, 100, "Pimienta rosa, Rosa de Bulgaria", catArabeF));
        listaPerfumes.add(new Perfume(id++, "Honor & Glory (Bade'e Al Oud)", "Lattafa", 240000.0, 100, "Piña, Brulee, Canela", catArabeF));
        listaPerfumes.add(new Perfume(id++, "Eclat Extraordinaire", "Lattafa", 210000.0, 100, "Notas florales, Ámbar", catArabeF));
        listaPerfumes.add(new Perfume(id++, "Mayar", "Lattafa", 220000.0, 100, "Frambuesa, Litchi, Violeta", catArabeF));
        listaPerfumes.add(new Perfume(id++, "Mayar Natural Intense", "Lattafa", 230000.0, 100, "Higo, Agua de coco, Vainilla", catArabeF));
        listaPerfumes.add(new Perfume(id++, "Qaed Al Fursan Unlimited (Blanco)", "Lattafa", 190000.0, 90, "Coco, Cítricos, Ylang-Ylang", catArabeF));
        listaPerfumes.add(new Perfume(id++, "Musk Mood", "Lattafa", 180000.0, 100, "Cítricos, Almizcle blanco, Floral", catArabeF));
        listaPerfumes.add(new Perfume(id++, "Pure Musk", "Lattafa", 180000.0, 100, "Almizcle blanco, Notas dulces", catArabeF));
        listaPerfumes.add(new Perfume(id++, "Haya", "Lattafa", 220000.0, 100, "Champán rosa, Fresas, Orquídea", catArabeF));
        listaPerfumes.add(new Perfume(id++, "Sakeena", "Lattafa", 230000.0, 100, "Maracuyá, Mandarina, Rosa", catArabeF));
        listaPerfumes.add(new Perfume(id++, "Mohra Silky Rose", "Lattafa", 210000.0, 100, "Rosa, Pimienta rosa, Almizcle", catArabeF));
        listaPerfumes.add(new Perfume(id++, "Nebras", "Lattafa", 260000.0, 100, "Bayas rojas, Mandarina, Cacao, Vainilla", catArabeF));
        listaPerfumes.add(new Perfume(id++, "Ansaam Gold", "Lattafa", 270000.0, 100, "Pera, Mandarina, Jazmín, Vainilla", catArabeF));

        // MAISON ALHAMBRA
        listaPerfumes.add(new Perfume(id++, "Delilah", "Maison Alhambra", 190000.0, 100, "Litchi, Ruibarbo, Bergamota, Rosa", catArabeF));
        listaPerfumes.add(new Perfume(id++, "Exclusif Rose", "Maison Alhambra", 210000.0, 100, "Rosa, Cítricos, Ámbar", catArabeF));
        listaPerfumes.add(new Perfume(id++, "Exclusif Saffron", "Maison Alhambra", 210000.0, 100, "Azafrán, Cuero, Especias", catArabeF));
        listaPerfumes.add(new Perfume(id++, "Porto Neroli", "Maison Alhambra", 190000.0, 100, "Neroli, Cítricos, Jazmín", catArabeF));
        listaPerfumes.add(new Perfume(id++, "Bright Peach", "Maison Alhambra", 190000.0, 100, "Melocotón, Naranja sanguínea, Ron", catArabeF));
        listaPerfumes.add(new Perfume(id++, "Lovely Cherie", "Maison Alhambra", 190000.0, 100, "Cereza negra, Almendra amarga, Licor", catArabeF));
        listaPerfumes.add(new Perfume(id++, "Rose Petals", "Maison Alhambra", 190000.0, 100, "Pimienta de Sichuan, Rosa de mayo", catArabeF));
        listaPerfumes.add(new Perfume(id++, "Infini Rose", "Maison Alhambra", 200000.0, 100, "Bergamota, Pimienta rosa, Rosa", catArabeF));
        listaPerfumes.add(new Perfume(id++, "Narissa For Her", "Maison Alhambra", 190000.0, 100, "Flor de azahar, Almizcle, Ámbar", catArabeF));
        listaPerfumes.add(new Perfume(id++, "Kismet For Women", "Maison Alhambra", 200000.0, 100, "Damasco, Jazmín, Tuberosa", catArabeF));

        // ARMAF
        listaPerfumes.add(new Perfume(id++, "Club de Nuit Woman", "Armaf", 240000.0, 105, "Naranja, Bergamota, Rosa, Jazmín", catArabeF));
        listaPerfumes.add(new Perfume(id++, "Club de Nuit Imperiale", "Armaf", 290000.0, 105, "Litchi, Bergamota, Rosa turca, Vainilla", catArabeF));
        listaPerfumes.add(new Perfume(id++, "Club de Nuit Untold", "Armaf", 350000.0, 105, "Azafrán, Jazmín, Madera de Ámbar", catArabeF));
        listaPerfumes.add(new Perfume(id++, "Club de Nuit Milestone", "Armaf", 280000.0, 105, "Notas marinas, Cítricos, Violeta", catArabeF));
        listaPerfumes.add(new Perfume(id++, "Odyssey Candleglow", "Armaf", 240000.0, 100, "Flores blancas, Vainilla", catArabeF));
        listaPerfumes.add(new Perfume(id++, "Odyssey Femme", "Armaf", 230000.0, 100, "Naranja, Tuberosa, Jasmin", catArabeF));
        listaPerfumes.add(new Perfume(id++, "Le Parfait Pour Femme", "Armaf", 210000.0, 100, "Jazmín, Mandarina, Tuberosa", catArabeF));
        listaPerfumes.add(new Perfume(id++, "Vanity Femme Essence", "Armaf", 190000.0, 100, "Fresa, Melocotón, Flor de Azahar", catArabeF));
        listaPerfumes.add(new Perfume(id++, "Tres Nuit Valentina", "Armaf", 220000.0, 100, "Cítricos, Rosa, Almizcle", catArabeF));

        // ORIENTICA
        listaPerfumes.add(new Perfume(id++, "Velvet Gold", "Orientica", 450000.0, 80, "Pimienta rosa, Violeta, Vainilla", catArabeF));
        listaPerfumes.add(new Perfume(id++, "Amber Rouge", "Orientica", 420000.0, 80, "Jazmín, Azafrán, Ámbar gris", catArabeF));
        listaPerfumes.add(new Perfume(id++, "Royal Amber", "Orientica", 420000.0, 80, "Bergamota, Melón, Ámbar", catArabeF));
        listaPerfumes.add(new Perfume(id++, "Fantasy", "Orientica", 380000.0, 80, "Nuez moscada, Manzanilla, Ámbar", catArabeF));
        listaPerfumes.add(new Perfume(id++, "Arte Bellissimo Romantic", "Orientica", 380000.0, 80, "Bergamota, Melocotón, Rosa", catArabeF));
        listaPerfumes.add(new Perfume(id++, "Fleur de Orientica", "Orientica", 390000.0, 80, "Flores blancas, Cítricos", catArabeF));

        // AFNAN
        listaPerfumes.add(new Perfume(id++, "9 AM Pour Femme", "Afnan", 220000.0, 100, "Mandarina, Bergamota, Toronja, Grosellas", catArabeF));
        listaPerfumes.add(new Perfume(id++, "9 PM Pour Femme", "Afnan", 220000.0, 100, "Frambuesa, Manzana, Violeta, Vainilla", catArabeF));
        listaPerfumes.add(new Perfume(id++, "Zimaya Sharaf Blend", "Afnan", 240000.0, 100, "Datiles, Canela, Vainilla", catArabeF));
        listaPerfumes.add(new Perfume(id++, "Supremacy Pink", "Afnan", 250000.0, 100, "Rosa, Violeta, Almizcle", catArabeF));
        listaPerfumes.add(new Perfume(id++, "Modest Deux", "Afnan", 230000.0, 100, "Frutos oscuros, Chocolate, Vainilla", catArabeF));
        listaPerfumes.add(new Perfume(id++, "Historic Doria", "Afnan", 250000.0, 100, "Flor de Azahar, Tuberosa, Jasmin", catArabeF));

        // AL HARAMAIN
        listaPerfumes.add(new Perfume(id++, "Amber Oud Gold Edition", "Al Haramain", 380000.0, 120, "Bergamota, Melón, Piña", catArabeF));
        listaPerfumes.add(new Perfume(id++, "Amber Oud Ultra Violet", "Al Haramain", 390000.0, 120, "Jazmín, Rosa, Cedro", catArabeF));
        listaPerfumes.add(new Perfume(id++, "L'Aventure Femme", "Al Haramain", 280000.0, 100, "Piña, Grosella negra, Bergamota", catArabeF));
        listaPerfumes.add(new Perfume(id++, "Junoon Rose", "Al Haramain", 340000.0, 75, "Flor de Cerezo, Amaretto, Rosa", catArabeF));


        // ==========================================
        // 4. PERFUMERÍA FEMENINA (47 Perfumes)
        // ==========================================
        // CAROLINA HERRERA
        listaPerfumes.add(new Perfume(id++, "Good Girl", "Carolina Herrera", 580000.0, 80, "Almendra, Café, Jazmín Sambac, Tuberosa", catFemenina));
        listaPerfumes.add(new Perfume(id++, "Good Girl Blush", "Carolina Herrera", 610000.0, 80, "Bergamota, Peonía, Vainilla", catFemenina));
        listaPerfumes.add(new Perfume(id++, "Very Good Girl", "Carolina Herrera", 610000.0, 80, "Grosella roja, Litchi, Rosa", catFemenina));
        listaPerfumes.add(new Perfume(id++, "Very Good Girl Elixir", "Carolina Herrera", 640000.0, 80, "Cereza, Rosa, Vainilla", catFemenina));
        listaPerfumes.add(new Perfume(id++, "212 VIP Rosé", "Carolina Herrera", 490000.0, 80, "Champán rosado, Flor de durazno, Amaderado", catFemenina));
        listaPerfumes.add(new Perfume(id++, "212 VIP Rosé Elixir", "Carolina Herrera", 530000.0, 80, "Licor de cereza, Flor de azahar", catFemenina));

        // BOND NO. 9
        listaPerfumes.add(new Perfume(id++, "Greenwich Village", "BOND No. 9", 1450000.0, 100, "Cassis, Lychee, Mandarina, Peonía", catFemenina));
        listaPerfumes.add(new Perfume(id++, "Tribeca", "BOND No. 9", 1450000.0, 100, "Cacao, Avellana, Jazmín Sambac", catFemenina));
        listaPerfumes.add(new Perfume(id++, "Chez Bond / Nolita", "BOND No. 9", 1350000.0, 100, "Té verde, Cítricos, Violeta", catFemenina));
        listaPerfumes.add(new Perfume(id++, "Central Park West", "BOND No. 9", 1350000.0, 100, "Narciso, Ylang-Ylang, Pimienta negra", catFemenina));
        listaPerfumes.add(new Perfume(id++, "Gold Coast", "BOND No. 9", 140000.0, 100, "Litchi, Damasco, Rosa de Francia", catFemenina));
        listaPerfumes.add(new Perfume(id++, "Chinatown", "BOND No. 9", 1350000.0, 100, "Flor de Melocotón, Bergamota, Tuberosa", catFemenina));

        // VALENTINO
        listaPerfumes.add(new Perfume(id++, "Donna Born in Roma", "Valentino", 630000.0, 100, "Grosellas negras, Bergamota, Jazmín Sambac", catFemenina));
        listaPerfumes.add(new Perfume(id++, "Donna Born in Roma Coral Fantasy", "Valentino", 650000.0, 100, "Kiwi, Naranja de Brasil, Rosa", catFemenina));
        listaPerfumes.add(new Perfume(id++, "Donna Born in Roma Yellow Dream", "Valentino", 650000.0, 100, "Limón primofiore, Rosa, Almizcle", catFemenina));
        listaPerfumes.add(new Perfume(id++, "Donna Born in Roma Intense", "Valentino", 700000.0, 100, "Vainilla Bourbon, Jazmín, Benzoin", catFemenina));

        // ARIANA GRANDE
        listaPerfumes.add(new Perfume(id++, "Sweet Like Candy", "Ariana Grande", 380000.0, 100, "Mora zarzamora, Bergamota, Crema batida", catFemenina));
        listaPerfumes.add(new Perfume(id++, "Thank U, Next", "Ariana Grande", 390000.0, 100, "Pera blanca, Frambuesa, Coco", catFemenina));
        listaPerfumes.add(new Perfume(id++, "Cloud Eau de Parfum", "Ariana Grande", 420000.0, 100, "Lavanda, Pera, Bergamota, Crema de coco", catFemenina));
        listaPerfumes.add(new Perfume(id++, "Cloud Pink", "Ariana Grande", 460000.0, 100, "Pitahaya, Bayas silvestres, Agua de coco", catFemenina));

        // MOSCHINO
        listaPerfumes.add(new Perfume(id++, "Toy 2 Bubble Gum", "Moschino", 360000.0, 100, "Fruta confitada, Chicle, Naranja, Rosa", catFemenina));
        listaPerfumes.add(new Perfume(id++, "Fresh Couture / Pink Fresh", "Moschino", 340000.0, 100, "Toronja rosada, CASSIS, Lirio del valle", catFemenina));
        listaPerfumes.add(new Perfume(id++, "Gold Fresh Couture", "Moschino", 350000.0, 100, "Mango, Mandarina, Pera, Orquídea", catFemenina));

        // JEAN PAUL GAULTIER
        listaPerfumes.add(new Perfume(id++, "Scandal Eau de Parfum", "Jean Paul Gaultier", 530000.0, 80, "Naranja sanguínea, Miel, Gardenia", catFemenina));
        listaPerfumes.add(new Perfume(id++, "So Scandal!", "Jean Paul Gaultier", 560000.0, 80, "Flor de Azahar, Tuberosa, Jazmín Sambac", catFemenina));
        listaPerfumes.add(new Perfume(id++, "La Belle Le Parfum", "Jean Paul Gaultier", 580000.0, 100, "Pera, Tonka, Vainilla", catFemenina));

        // ILMIN
        listaPerfumes.add(new Perfume(id++, "Il Rosa", "Ilmin", 620000.0, 30, "Rosa, Notas dulces", catFemenina));
        listaPerfumes.add(new Perfume(id++, "Il Fiori", "Ilmin", 620000.0, 30, "Flores blancas, Cítricos", catFemenina));
        listaPerfumes.add(new Perfume(id++, "Il Dolce", "Ilmin", 620000.0, 30, "Gourmand, Vainilla", catFemenina));
        listaPerfumes.add(new Perfume(id++, "Il Femme", "Ilmin", 620000.0, 30, "Floral, Amaderado", catFemenina));
        listaPerfumes.add(new Perfume(id++, "Il Carmine", "Ilmin", 620000.0, 30, "Notas amaderadas, Ámbar", catFemenina));
        listaPerfumes.add(new Perfume(id++, "Il Sesso", "Ilmin", 620000.0, 30, "Cítricos, Almizcle", catFemenina));

        // DEVIER
        listaPerfumes.add(new Perfume(id++, "Satin Rose", "Devier", 380000.0, 100, "Rosa, Almizcle", catFemenina));
        listaPerfumes.add(new Perfume(id++, "VIP Carat", "Devier", 390000.0, 100, "Especias finas, Ámbar", catFemenina));
        listaPerfumes.add(new Perfume(id++, "Amber Black", "Devier", 380000.0, 100, "Ámbar, Madera", catFemenina));
        listaPerfumes.add(new Perfume(id++, "Lermo", "Devier", 380000.0, 100, "Cítrico, Amaderado", catFemenina));

        // OTRAS MARCAS INTERNACIONALES
        listaPerfumes.add(new Perfume(id++, "Libre Eau de Parfum", "Yves Saint Laurent", 620000.0, 90, "Lavanda, Mandarina, Grosellas negras", catFemenina));
        listaPerfumes.add(new Perfume(id++, "Black Opium", "Yves Saint Laurent", 580000.0, 90, "Pera, Pimienta rosa, Flor de azahar, Café", catFemenina));
        listaPerfumes.add(new Perfume(id++, "La Vie Est Belle", "Lancôme", 520000.0, 100, "Grosellas negras, Pera, Iris, Jazmín", catFemenina));
        listaPerfumes.add(new Perfume(id++, "Idôle", "Lancôme", 490000.0, 100, "Pera, Bergamota, Rosa de Esparta, Jazmín", catFemenina));
        listaPerfumes.add(new Perfume(id++, "Bright Crystal", "Versace", 430000.0, 90, "Yuzu, Granada, Peonía, Flor de loto", catFemenina));
        listaPerfumes.add(new Perfume(id++, "Yellow Diamond", "Versace", 410000.0, 90, "Limón de Amalfi, Bergamota, Neroli", catFemenina));
        listaPerfumes.add(new Perfume(id++, "Miss Dior Eau de Parfum", "Dior", 680000.0, 100, "Lirio del valle, Peonía, Iris, Rosa de centifolia", catFemenina));
        listaPerfumes.add(new Perfume(id++, "Coco Mademoiselle", "Chanel", 780000.0, 100, "Naranja, Mandarina, Bergamota, Mimosa", catFemenina));
        listaPerfumes.add(new Perfume(id++, "Daisy Love", "Marc Jacobs", 420000.0, 100, "Mora de los pantanos, Margaritas, Almizcle", catFemenina));
        listaPerfumes.add(new Perfume(id++, "Her Eau de Parfum", "Burberry", 520000.0, 100, "Fresa, Frambuesa, Mora zarzamora, Guinda", catFemenina));
        listaPerfumes.add(new Perfume(id++, "Olympea", "Paco Rabanne", 490000.0, 80, "Mandarina verde, Jazmín de agua, Flor de jengibre", catFemenina));

        return listaPerfumes;
    }
}
