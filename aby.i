%module Aby

// Use proper Java enums for translating C++ enums.
%include "enums.swg"
%javaconst(1);

// Recognize standard types.
%include "stdint.i"
%include "std_string.i"
%include "std_vector.i"

// Ignore everything by default then selectively include.
%ignore "";

// Don't ignore members.
%rename("%s", %$isenumitem) "";
%rename("%(lowercamelcase)s", %$ismember) "";
%rename("%s", %$isconstructor) "";


// Selectively include.

%rename("%s", %$isclass) "ABYParty";

// ABYParty::GetSharings returns a reference to a vector of pointers.
// This is essentially impossible to use from Java, so we define a new
// function that does the lookup for us.
%ignore "ABYParty::GetSharings";
%extend ABYParty {
    Circuit* GetCircuitBuilder(e_sharing sharing) {
        return $self->GetSharings()[sharing]->GetCircuitBuildRoutine();
    }
};

%rename("Circuit") "Circuit";
%ignore "Circuit::~Circuit";
%ignore "Circuit::Init";
%ignore "Circuit::Cleanup";
%ignore "Circuit::Reset";
%ignore "Circuit::PutCondSwapGate";

%rename("Share") "share";
// %rename("ArithmeticShare") "arithshare";
// %rename("BooleanShare") "boolshare";
%ignore "share::~share";
%ignore "share::init";
%ignore "share::get_clear_value_ptr";
%ignore "share::get_clear_value_vec";
%rename("%(lowercamelcase)s") "create_new_share";

%rename("CircuitType") "e_circuit";
%rename("MultiplicationTripleGenerationAlgorithm") "e_mt_gen_alg";
%rename("Phase") "ABYPHASE";
%rename("Role") "e_role";
%rename("SharingType") "e_sharing";

%rename("SecurityLevel") "SECURITYLEVELS";
%rename("%s") "ST";
%rename("%s") "MT";
%rename("%s") "LT";
%rename("%s") "XLT";
%rename("%s") "XXLT";

%include "abycore/aby/abyparty.h"
%include "abycore/ABY_utils/ABYconstants.h"
%include "abycore/circuit/circuit.h"
%include "abycore/circuit/share.h"
%include "ENCRYPTO_utils/constants.h"
%include "ENCRYPTO_utils/crypto/crypto.h"
%include "ENCRYPTO_utils/timer.h"
%include "ENCRYPTO_utils/typedefs.h"

// Expand templates
%rename("getClearValue8") share::get_clear_value<uint8_t>;
%rename("getClearValue16") share::get_clear_value<uint16_t>;
%rename("getClearValue32") share::get_clear_value<uint32_t>;
%rename("getClearValue64") share::get_clear_value<uint64_t>;
%template() share::get_clear_value<uint8_t>;
%template() share::get_clear_value<uint16_t>;
%template() share::get_clear_value<uint32_t>;
%template() share::get_clear_value<uint64_t>;
