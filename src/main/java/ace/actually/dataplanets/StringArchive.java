package ace.actually.dataplanets;

public class StringArchive {

    //TODO: Somehow reduce the psychic damage the noise_settings list causes
    //TODO: noiseSizeX=3,noiseSizeY=3 crashes.
    private final String moon = "{\n" +
            "  \"sea_level\": 63,\n" +
            "  \"disable_mob_generation\": false,\n" +
            "  \"aquifers_enabled\": false,\n" +
            "  \"ore_veins_enabled\": false,\n" +
            "  \"legacy_random_source\": false,\n" +
            "  \"default_block\": {\n" +
            "    \"Name\": \"SB\"\n".replace("SB","generalBlock") +
            "  },\n" +
            "  \"default_fluid\": {\n" +
            "    \"Name\": \"minecraft:air\"\n" +
            "  },\n" +
            "  \"noise\": {\n" +
            "    \"min_y\": -64,\n" +
            "    \"height\": 384,\n" +
            "    \"size_horizontal\": H,\n".replace("H",""+"noiseSizeX") +
            "    \"size_vertical\": V\n".replace("V",""+"noiseSizeY") +
            "  },\n" +
            "  \"noise_router\": {\n" +
            "    \"barrier\": {\n" +
            "      \"type\": \"minecraft:noise\",\n" +
            "      \"noise\": \"minecraft:aquifer_barrier\",\n" +
            "      \"xz_scale\": 1,\n" +
            "      \"y_scale\": 0.5\n" +
            "    },\n" +
            "    \"fluid_level_floodedness\": {\n" +
            "      \"type\": \"minecraft:noise\",\n" +
            "      \"noise\": \"minecraft:aquifer_fluid_level_floodedness\",\n" +
            "      \"xz_scale\": 1,\n" +
            "      \"y_scale\": 0.67\n" +
            "    },\n" +
            "    \"fluid_level_spread\": {\n" +
            "      \"type\": \"minecraft:noise\",\n" +
            "      \"noise\": \"minecraft:aquifer_fluid_level_spread\",\n" +
            "      \"xz_scale\": 1,\n" +
            "      \"y_scale\": 0.7142857142857143\n" +
            "    },\n" +
            "    \"lava\": {\n" +
            "      \"type\": \"minecraft:noise\",\n" +
            "      \"noise\": \"minecraft:aquifer_lava\",\n" +
            "      \"xz_scale\": 1,\n" +
            "      \"y_scale\": 1\n" +
            "    },\n" +
            "    \"temperature\": {\n" +
            "      \"type\": \"minecraft:shifted_noise\",\n" +
            "      \"noise\": \"minecraft:temperature\",\n" +
            "      \"xz_scale\": 0.25,\n" +
            "      \"y_scale\": 0,\n" +
            "      \"shift_x\": \"minecraft:shift_x\",\n" +
            "      \"shift_y\": 0,\n" +
            "      \"shift_z\": \"minecraft:shift_z\"\n" +
            "    },\n" +
            "    \"vegetation\": {\n" +
            "      \"type\": \"minecraft:shifted_noise\",\n" +
            "      \"noise\": \"minecraft:vegetation\",\n" +
            "      \"xz_scale\": 0.25,\n" +
            "      \"y_scale\": 0,\n" +
            "      \"shift_x\": \"minecraft:shift_x\",\n" +
            "      \"shift_y\": 0,\n" +
            "      \"shift_z\": \"minecraft:shift_z\"\n" +
            "    },\n" +
            "    \"continents\": \"minecraft:overworld/continents\",\n" +
            "    \"ridges\": \"minecraft:overworld/ridges\",\n" +
            "    \"erosion\": \"minecraft:overworld/erosion\",\n" +
            "    \"depth\": \"gcyr:depth\",\n" +
            "    \"initial_density_without_jaggedness\": {\n" +
            "      \"type\": \"minecraft:mul\",\n" +
            "      \"argument1\": 4,\n" +
            "      \"argument2\": {\n" +
            "        \"type\": \"minecraft:quarter_negative\",\n" +
            "        \"argument\": {\n" +
            "          \"type\": \"minecraft:mul\",\n" +
            "          \"argument1\": \"gcyr:depth\",\n" +
            "          \"argument2\": {\n" +
            "            \"type\": \"minecraft:cache_2d\",\n" +
            "            \"argument\": \"gcyr:factor\"\n" +
            "          }\n" +
            "        }\n" +
            "      }\n" +
            "    },\n" +
            "    \"final_density\": {\n" +
            "      \"type\": \"minecraft:max\",\n" +
            "      \"argument1\": {\n" +
            "        \"type\": \"minecraft:y_clamped_gradient\",\n" +
            "        \"from_y\": -64,\n" +
            "        \"to_y\": -63,\n" +
            "        \"from_value\": 1,\n" +
            "        \"to_value\": -1\n" +
            "      },\n" +
            "      \"argument2\": {\n" +
            "        \"type\": \"minecraft:min\",\n" +
            "        \"argument1\": {\n" +
            "          \"type\": \"minecraft:squeeze\",\n" +
            "          \"argument\": {\n" +
            "            \"type\": \"minecraft:mul\",\n" +
            "            \"argument1\": 0.64,\n" +
            "            \"argument2\": {\n" +
            "              \"type\": \"minecraft:interpolated\",\n" +
            "              \"argument\": {\n" +
            "                \"type\": \"minecraft:blend_density\",\n" +
            "                \"argument\": {\n" +
            "                  \"type\": \"minecraft:add\",\n" +
            "                  \"argument1\": {\n" +
            "                    \"type\": \"minecraft:mul\",\n" +
            "                    \"argument1\": {\n" +
            "                      \"type\": \"minecraft:y_clamped_gradient\",\n" +
            "                      \"from_y\": 296,\n" +
            "                      \"to_y\": 320,\n" +
            "                      \"from_value\": 1,\n" +
            "                      \"to_value\": 0\n" +
            "                    },\n" +
            "                    \"argument2\": {\n" +
            "                      \"type\": \"minecraft:add\",\n" +
            "                      \"argument1\": {\n" +
            "                        \"type\": \"minecraft:range_choice\",\n" +
            "                        \"input\": \"gcyr:sloped_cheese\",\n" +
            "                        \"min_inclusive\": -1000000,\n" +
            "                        \"max_exclusive\": 1.5625,\n" +
            "                        \"when_in_range\": {\n" +
            "                          \"type\": \"minecraft:min\",\n" +
            "                          \"argument1\": \"gcyr:sloped_cheese\",\n" +
            "                          \"argument2\": {\n" +
            "                            \"type\": \"minecraft:mul\",\n" +
            "                            \"argument1\": 5,\n" +
            "                            \"argument2\": \"minecraft:overworld/caves/entrances\"\n" +
            "                          }\n" +
            "                        },\n" +
            "                        \"when_out_of_range\": {\n" +
            "                          \"type\": \"minecraft:max\",\n" +
            "                          \"argument1\": {\n" +
            "                            \"type\": \"minecraft:min\",\n" +
            "                            \"argument1\": {\n" +
            "                              \"type\": \"minecraft:min\",\n" +
            "                              \"argument1\": {\n" +
            "                                \"type\": \"minecraft:add\",\n" +
            "                                \"argument1\": {\n" +
            "                                  \"type\": \"minecraft:mul\",\n" +
            "                                  \"argument1\": 4,\n" +
            "                                  \"argument2\": {\n" +
            "                                    \"type\": \"minecraft:square\",\n" +
            "                                    \"argument\": {\n" +
            "                                      \"type\": \"minecraft:noise\",\n" +
            "                                      \"noise\": \"minecraft:cave_layer\",\n" +
            "                                      \"xz_scale\": NR,\n".replace("NR","nr1") +
            "                                      \"y_scale\": NR\n".replace("NR","nr2") +
            "                                    }\n" +
            "                                  }\n" +
            "                                },\n" +
            "                                \"argument2\": {\n" +
            "                                  \"type\": \"minecraft:add\",\n" +
            "                                  \"argument1\": {\n" +
            "                                    \"type\": \"minecraft:clamp\",\n" +
            "                                    \"input\": {\n" +
            "                                      \"type\": \"minecraft:add\",\n" +
            "                                      \"argument1\": 0.27,\n" +
            "                                      \"argument2\": {\n" +
            "                                        \"type\": \"minecraft:noise\",\n" +
            "                                        \"noise\": \"minecraft:cave_cheese\",\n" +
            "                                        \"xz_scale\": NR,\n".replace("NR","nr3") +
            "                                        \"y_scale\": NR\n".replace("NR","nr1") +
            "                                      }\n" +
            "                                    },\n" +
            "                                    \"min\": -1,\n" +
            "                                    \"max\": 1\n" +
            "                                  },\n" +
            "                                  \"argument2\": {\n" +
            "                                    \"type\": \"minecraft:clamp\",\n" +
            "                                    \"input\": {\n" +
            "                                      \"type\": \"minecraft:add\",\n" +
            "                                      \"argument1\": 1.5,\n" +
            "                                      \"argument2\": {\n" +
            "                                        \"type\": \"minecraft:mul\",\n" +
            "                                        \"argument1\": -0.64,\n" +
            "                                        \"argument2\": \"gcyr:sloped_cheese\"\n" +
            "                                      }\n" +
            "                                    },\n" +
            "                                    \"min\": 0,\n" +
            "                                    \"max\": 0.5\n" +
            "                                  }\n" +
            "                                }\n" +
            "                              },\n" +
            "                              \"argument2\": \"minecraft:overworld/caves/entrances\"\n" +
            "                            },\n" +
            "                            \"argument2\": {\n" +
            "                              \"type\": \"minecraft:add\",\n" +
            "                              \"argument1\": \"minecraft:overworld/caves/spaghetti_2d\",\n" +
            "                              \"argument2\": \"minecraft:overworld/caves/spaghetti_roughness_function\"\n" +
            "                            }\n" +
            "                          },\n" +
            "                          \"argument2\": {\n" +
            "                            \"type\": \"minecraft:range_choice\",\n" +
            "                            \"input\": \"minecraft:overworld/caves/pillars\",\n" +
            "                            \"min_inclusive\": -1000000,\n" +
            "                            \"max_exclusive\": 0.03,\n" +
            "                            \"when_in_range\": -1000000,\n" +
            "                            \"when_out_of_range\": \"minecraft:overworld/caves/pillars\"\n" +
            "                          }\n" +
            "                        }\n" +
            "                      },\n" +
            "                      \"argument2\": 10\n" +
            "                    }\n" +
            "                  },\n" +
            "                  \"argument2\": -10\n" +
            "                }\n" +
            "              }\n" +
            "            }\n" +
            "          }\n" +
            "        },\n" +
            "        \"argument2\": \"minecraft:overworld/caves/noodle\"\n" +
            "      }\n" +
            "    },\n" +
            "    \"vein_toggle\": {\n" +
            "      \"type\": \"minecraft:interpolated\",\n" +
            "      \"argument\": {\n" +
            "        \"type\": \"minecraft:range_choice\",\n" +
            "        \"input\": \"minecraft:y\",\n" +
            "        \"min_inclusive\": -60,\n" +
            "        \"max_exclusive\": 51,\n" +
            "        \"when_in_range\": {\n" +
            "          \"type\": \"minecraft:noise\",\n" +
            "          \"noise\": \"minecraft:ore_veininess\",\n" +
            "          \"xz_scale\": 1.5,\n" +
            "          \"y_scale\": 1.5\n" +
            "        },\n" +
            "        \"when_out_of_range\": 0\n" +
            "      }\n" +
            "    },\n" +
            "    \"vein_ridged\": {\n" +
            "      \"type\": \"minecraft:add\",\n" +
            "      \"argument1\": -0.07999999821186066,\n" +
            "      \"argument2\": {\n" +
            "        \"type\": \"minecraft:max\",\n" +
            "        \"argument1\": {\n" +
            "          \"type\": \"minecraft:abs\",\n" +
            "          \"argument\": {\n" +
            "            \"type\": \"minecraft:interpolated\",\n" +
            "            \"argument\": {\n" +
            "              \"type\": \"minecraft:range_choice\",\n" +
            "              \"input\": \"minecraft:y\",\n" +
            "              \"min_inclusive\": -60,\n" +
            "              \"max_exclusive\": 51,\n" +
            "              \"when_in_range\": {\n" +
            "                \"type\": \"minecraft:noise\",\n" +
            "                \"noise\": \"minecraft:ore_vein_a\",\n" +
            "                \"xz_scale\": 4,\n" +
            "                \"y_scale\": 4\n" +
            "              },\n" +
            "              \"when_out_of_range\": 0\n" +
            "            }\n" +
            "          }\n" +
            "        },\n" +
            "        \"argument2\": {\n" +
            "          \"type\": \"minecraft:abs\",\n" +
            "          \"argument\": {\n" +
            "            \"type\": \"minecraft:interpolated\",\n" +
            "            \"argument\": {\n" +
            "              \"type\": \"minecraft:range_choice\",\n" +
            "              \"input\": \"minecraft:y\",\n" +
            "              \"min_inclusive\": -60,\n" +
            "              \"max_exclusive\": 51,\n" +
            "              \"when_in_range\": {\n" +
            "                \"type\": \"minecraft:noise\",\n" +
            "                \"noise\": \"minecraft:ore_vein_b\",\n" +
            "                \"xz_scale\": 4,\n" +
            "                \"y_scale\": 4\n" +
            "              },\n" +
            "              \"when_out_of_range\": 0\n" +
            "            }\n" +
            "          }\n" +
            "        }\n" +
            "      }\n" +
            "    },\n" +
            "    \"vein_gap\": {\n" +
            "      \"type\": \"minecraft:noise\",\n" +
            "      \"noise\": \"minecraft:ore_gap\",\n" +
            "      \"xz_scale\": 1,\n" +
            "      \"y_scale\": 1\n" +
            "    }\n" +
            "  },\n" +
            "  \"surface_rule\": {\n" +
            "    \"type\": \"minecraft:sequence\",\n" +
            "    \"sequence\": [\n" +
            "      {\n" +
            "        \"type\": \"minecraft:condition\",\n" +
            "        \"if_true\": {\n" +
            "          \"type\": \"minecraft:vertical_gradient\",\n" +
            "          \"random_name\": \"minecraft:bedrock_floor\",\n" +
            "          \"true_at_and_below\": {\n" +
            "            \"above_bottom\": 0\n" +
            "          },\n" +
            "          \"false_at_and_above\": {\n" +
            "            \"above_bottom\": 5\n" +
            "          }\n" +
            "        },\n" +
            "        \"then_run\": {\n" +
            "          \"type\": \"minecraft:block\",\n" +
            "          \"result_state\": {\n" +
            "            \"Name\": \"minecraft:bedrock\"\n" +
            "          }\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"type\": \"minecraft:condition\",\n" +
            "        \"if_true\": {\n" +
            "          \"type\": \"minecraft:biome\",\n" +
            "          \"biome_is\": [\n" +
            "            \"gcyr:moon\"\n" +
            "          ]\n" +
            "        },\n" +
            "        \"then_run\": {\n" +
            "          \"type\": \"minecraft:condition\",\n" +
            "          \"if_true\": {\n" +
            "            \"type\": \"minecraft:y_above\",\n" +
            "            \"anchor\": {\n" +
            "              \"absolute\": 87\n" +
            "            },\n" +
            "            \"surface_depth_multiplier\": 2,\n" +
            "            \"add_stone_depth\": false\n" +
            "          },\n" +
            "          \"then_run\": {\n" +
            "            \"type\": \"minecraft:condition\",\n" +
            "            \"if_true\": {\n" +
            "              \"type\": \"minecraft:stone_depth\",\n" +
            "              \"offset\": 4,\n" +
            "              \"surface_type\": \"floor\",\n" +
            "              \"add_surface_depth\": false,\n" +
            "              \"secondary_depth_range\": 0\n" +
            "            },\n" +
            "            \"then_run\": {\n" +
            "              \"type\": \"minecraft:block\",\n" +
            "              \"result_state\": {\n" +
            "                \"Name\": \"gcyr:moon_sand\"\n" +
            "              }\n" +
            "            }\n" +
            "          }\n" +
            "        }\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"spawn_target\": [\n" +
            "    {\n" +
            "      \"erosion\": [\n" +
            "        -1,\n" +
            "        1\n" +
            "      ],\n" +
            "      \"depth\": 0,\n" +
            "      \"weirdness\": [\n" +
            "        -1,\n" +
            "        -0.16\n" +
            "      ],\n" +
            "      \"offset\": 0,\n" +
            "      \"temperature\": [\n" +
            "        -1,\n" +
            "        1\n" +
            "      ],\n" +
            "      \"humidity\": [\n" +
            "        -1,\n" +
            "        1\n" +
            "      ],\n" +
            "      \"continentalness\": [\n" +
            "        -0.11,\n" +
            "        1\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"erosion\": [\n" +
            "        -1,\n" +
            "        1\n" +
            "      ],\n" +
            "      \"depth\": 0,\n" +
            "      \"weirdness\": [\n" +
            "        0.16,\n" +
            "        1\n" +
            "      ],\n" +
            "      \"offset\": 0,\n" +
            "      \"temperature\": [\n" +
            "        -1,\n" +
            "        1\n" +
            "      ],\n" +
            "      \"humidity\": [\n" +
            "        -1,\n" +
            "        1\n" +
            "      ],\n" +
            "      \"continentalness\": [\n" +
            "        -0.11,\n" +
            "        1\n" +
            "      ]\n" +
            "    }\n" +
            "  ]\n" +
            "}";
}
