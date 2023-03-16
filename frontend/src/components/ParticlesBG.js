import React, {useCallback, useMemo} from "react";
import Particles from "react-tsparticles";
import { loadSlim } from "tsparticles-slim";


export default function ParticlesBG () {
    const particlesInit = useCallback(async (engine) => {
        await loadSlim(engine);
    }, []);

    const optionsBig = useMemo(() => {
        return {
            style: "App.css",
            fullScreen: false,
            fpsLimit: 60,
            particles: {
                shadow: {
                    enable: true,
                    blur: 50,
                    color: "#FFFFFF"
                },
                number: {
                    value: 80,
                    density: {
                        enable: true,
                        value_area: 400
                    }
                },
                color: {
                    value: "#ffffff"
                },
                shape: {
                    type: "circle",
                },
                opacity: {
                    value: 0.5,
                    random: true,
                    anim: {
                        enable: true,
                        speed: {
                            min: 0.05,
                            max: 0.075
                        },
                        opacity_min: 0,
                        startValue: "random",
                        sync: false
                    }
                },
                size: {
                    value: 20,
                    random: {
                        minimumValue: 0,
                        enable: true,
                    },
                    anim: {
                        enable: true,
                        speed: {
                            min: 1,
                            max: 10
                        },
                        size_min: 0,
                        sync: false
                    }
                },
                move: {
                    enable: true,
                    speed: {
                        min: 0.1,
                        max: 0.4
                    },
                    direction: "none",
                    random: true,
                    straight: false,
                    out_mode: "out",
                    bounce: false,
                }
            },
            detectRetina: true,
        }
    },[])
    // const optionsFar = useMemo(() => {
    //     return {
    //         style: "App.css",
    //         fullScreen: false,
    //         fpsLimit: 60,
    //         particles: {
    //             shadow: {
    //                 enable: true,
    //                 blur: 50,
    //                 color: "#255"
    //             },
    //             number: {
    //                 value: 10,
    //                 density: {
    //                     enable: true,
    //                     value_area: 100
    //                 }
    //             },
    //             color: {
    //                 value: "#1c3131"
    //             },
    //             shape: {
    //                 type: "circle",
    //             },
    //             opacity: {
    //                 value: 1,
    //                 random: {
    //                     minimumValue: 0.2,
    //                     enable: true
    //                 },
    //                 anim: {
    //                     enable: true,
    //                     speed: {
    //                         min: 0.05,
    //                         max: 0.075
    //                     },
    //                     startValue: "random",
    //                     sync: false
    //                 }
    //             },
    //             size: {
    //                 value: 5,
    //                 random: true,
    //                 anim: {
    //                     enable: true,
    //                     speed: {
    //                         min: 1,
    //                         max: 3
    //                     },
    //                     sync: false
    //                 }
    //             },
    //             move: {
    //                 enable: true,
    //                 speed: {
    //                     min: 0.5,
    //                     max: 5
    //                 },
    //                 direction: "right",
    //                 straight: false,
    //                 out_mode: "out",
    //                 bounce: false,
    //             }
    //         },
    //         detectRetina: true,
    //     }
    // },[])
    return (
        <div>
            <Particles
                id="particles-js-big"
                init={particlesInit}
                options={optionsBig}
            />
            {/*<Particles*/}
            {/*    id="particles-js-far"*/}
            {/*    init={particlesInit}*/}
            {/*    options={optionsFar}*/}
            {/*/>*/}
        </div>
    );



}