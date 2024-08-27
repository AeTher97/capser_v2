import React from 'react';
import FetchSelectField from "./FetchSelectField";

const UserFetchSelectField = ({onChange, omitAlso, searchYourself}) => {
    return (
        <FetchSelectField label={"Select Opponent"} onChange={onChange}
                          url={"/users/search"}
                          nameParameter={"username"} omitAlso={omitAlso} searchYourself={omitAlso}/>
    );
};

export default UserFetchSelectField;
