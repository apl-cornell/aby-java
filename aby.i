%module Aby

%include "enums.swg"
%javaconst(1);

// Standard types.
%include "stdint.i"
%include "std_string.i"
%include "std_vector.i"

// Use camelCase for functions.
%rename("%(lowercamelcase)s", %$isfunction) "";
// Use CamelCase for classes.
%rename("%(camelcase)s", %$isclass) "";

%ignore "GetSharings";
%extend ABYParty {
    Circuit* GetCircuitBuilder(e_sharing sharing) {
        return $self->GetSharings()[sharing]->GetCircuitBuildRoutine();
    }
};

%ignore "arithshare";
%ignore "boolshare";
%ignore "non_lin_on_layers";
%ignore "PutCondSwapGate";
// %rename("ArithmeticShare", %$isclass) "arithshare";
// %rename("BooleanShare", %$isclass) "boolshare";

%include "abycore/aby/abyparty.h"
%include "abycore/circuit/circuit.h"
%include "abycore/circuit/share.h"
// %include "abycore/sharing/sharing.h"

// Ignore everything by default then selectively enable.
%ignore "";

// Don't ignore enum constants and struct fields.
%rename("%s", %$isenumitem) "";
%rename("%s", %$ismember) "";

// Strip unnecessary prefixes from enums and use CamelCase.
%rename("CircuitType") "e_circuit";
%rename("MultiplicationTripleGenerationAlgorithm") "e_mt_gen_alg";
%rename("Phase") "ABYPHASE";
// %rename("PreComputationPhase") "ePreCompPhase";
%rename("Role") "e_role";
%rename("SharingType") "e_sharing";

// Include select structs.
%rename("SecurityLevel") "SECURITYLEVELS";

%include "abycore/ABY_utils/ABYconstants.h"
%include "ENCRYPTO_utils/timer.h"
%include "ENCRYPTO_utils/typedefs.h"

