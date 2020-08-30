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
%ignore "GetSharings";
%extend ABYParty {
    Circuit* GetCircuitBuilder(e_sharing sharing) {
        return $self->GetSharings()[sharing]->GetCircuitBuildRoutine();
    }
};

%rename("Circuit") "Circuit";

%rename("Share") "share";
%ignore "PutCondSwapGate";
// %rename("ArithmeticShare", %$isclass) "arithshare";
// %rename("BooleanShare", %$isclass) "boolshare";

%rename("CircuitType") "e_circuit";
%rename("MultiplicationTripleGenerationAlgorithm") "e_mt_gen_alg";
%rename("Phase") "ABYPHASE";
// %rename("PreComputationPhase") "ePreCompPhase";
%rename("Role") "e_role";
%rename("SharingType") "e_sharing";
%rename("SecurityLevel") "SECURITYLEVELS";

%include "abycore/aby/abyparty.h"
%include "abycore/ABY_utils/ABYconstants.h"
%include "abycore/circuit/circuit.h"
%include "abycore/circuit/share.h"
// %include "abycore/sharing/sharing.h"
%include "ENCRYPTO_utils/timer.h"
%include "ENCRYPTO_utils/typedefs.h"
