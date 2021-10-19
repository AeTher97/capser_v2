import React from 'react';
import FetchSelectField from "./FetchSelectField";

const UserFetchSelectField = ({label, onChange}) => {
    return (
        <FetchSelectField label={"Select Opponent"} onChange={onChange}
                          url={"/users/search"}
                          nameParameter={"username"}/>
    );
};

export default UserFetchSelectField;
