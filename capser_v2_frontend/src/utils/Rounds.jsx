
export const RO_64=64;
export const RO_32=32;
export const RO_16=16;
export const RO_8=8;
export const RO_4=4;
export const RO_2=2;

const entryValues = [RO_64,RO_32,RO_16,RO_8,RO_4,RO_2]

export const getLevel = (entry) =>{
    let number =0;
    for(let i=0;i<entryValues.length;i++){
        if(entryValues[i] < entry){
            number++;
        }
    }
    return number;
}

export  const getCoordinatesIdsBelow = (entry) =>{
    let number =0;
    for(let i=0;i<entryValues.length;i++){
        if(entryValues[i] > entry){
            number += entry[i]/2;
        }
    }
    return number;
}

export  const getCoordinatesIdsBelowAndEqual= (entry) =>{
    let number =0;
    for(let i=0;i<entryValues.length;i++){
        if(entryValues[i] >= entry){
            number += entry[i]/2;
        }
    }
    return number;
}

export  const getCoordinatesIdsAbove= (entry) =>{
    let number =0;
    for(let i=0;i<entryValues.length;i++){
        if(entryValues[i] < entry){
            number += entry[i]/2;
        }
    }
    return number;
}

export  const getCoordinatesIdsAboveAndEqual= (entry) =>{
    let number =0;
    for(let i=0;i<entryValues.length;i++){
        if(entryValues[i] <= entry){
            number += entry[i]/2;
        }
    }
    return number;
}
