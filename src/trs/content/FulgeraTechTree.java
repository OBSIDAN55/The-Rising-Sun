package trs.content;


import static mindustry.content.TechTree.*;

public class FulgeraTechTree {
    public static void load(){


        Planets.fulgera.techTree = nodeRoot("fulgera",trsBlocks.Case,() -> {

            nodeRoot("walls", trsBlocks.clinovalveWall, () ->{
                node(trsBlocks.clinovalveWallLarge,()->{
                    node(trsBlocks.zincWall,()->{
                        node(trsBlocks.zincWallLarge,()->{
                            node(trsBlocks.steelWall,()->{
                                node(trsBlocks.steelWallLarge,()->{
                                    node(trsBlocks.exacrimWall,()->{
                                        node(trsBlocks.exacrimWallLarge);
                                    });
                                });
                            });
                        });
                    });
                    node(trsBlocks.carbonWall,()->{
                        node(trsBlocks.carbonWallLarge);
                    });
                });
            });
            nodeRoot("fractions",Planets.fulgera,()->{
                node(trsBlocks.acronyx);
                node(trsBlocks.arha);
                node(trsBlocks.hronos);
                node(trsBlocks.phoenix);
            });

            nodeRoot("fulgera-distribution",trsBlocks.clinovalveDuct, () -> {
                node(trsBlocks.clinovalveRouter, () ->{
                    node(trsBlocks.clinovalveSorter);
                    node(trsBlocks.clinovalveInvertedSorter);
                    node(trsBlocks.clinovalveOverflowGate);
                    node(trsBlocks.clinovalveUnderflowGate);
                });
                node(trsBlocks.clinovalveJunction);
                node(trsBlocks.clinovalveDuctBridge);
            });
            nodeRoot("drills",trsBlocks.hydraulicDrill,()->{
                node(trsBlocks.deepDrill,()->{
                    node(trsBlocks.clusterDrill);
                });
            });
            nodeRoot("items",trsItems.tin,()->{
                node(trsItems.clinovalve,()->{
                    node(trsItems.quartz,()->{
                        node(trsItems.quartzDust);
                    });
                    node(trsItems.biomass,()->{
                        node(trsItems.carbon,()->{
                            node(trsItems.carbonDust,()->{
                                node(trsItems.carbonGlass);
                            });
                        });
                    });
                    node(trsItems.zinc,()->{
                        node(trsItems.rubidium,()->{
                            node(trsItems.barium,()->{
                                node(trsItems.steel,()->{
                                    node(trsItems.chrome);
                                });
                            });
                        });
                    });
                });
            });
            nodeRoot("factories",trsBlocks.melter,()->{
                node(trsBlocks.brazier);
                node(trsBlocks.rubidiumSmelter);
                node(trsBlocks.atmosphericCondenser);
                node(trsBlocks.crusher,()-> {
                    node(trsBlocks.carbonGlassClin);
                });
            });
        });
    }
}
