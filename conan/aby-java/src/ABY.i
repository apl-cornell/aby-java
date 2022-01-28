%module Aby

// Use proper Java enums for translating C++ enums.
%include "enums.swg"
%javaconst(1);

// Recognize standard types.
%include "stdint.i"
%include "std_string.i"
%include "std_vector.i"
%template(UInt32Vector) std::vector<uint32_t>;

// Ignore everything by default then selectively include.
%ignore "";

// Don't ignore members.
%rename("%s", %$isenumitem) "";
%rename("%(lowercamelcase)s", %$ismember) "";
%rename("%s", %$isconstructor) "";


// Selectively include.

%rename("%s") ABYParty;

// ABYParty::GetSharings returns a reference to a vector of pointers.
// This is essentially impossible to use from Java, so we define a new
// function that does the lookup for us.
%ignore ABYParty::GetSharings;
%extend ABYParty {
    Circuit* GetCircuitBuilder(e_sharing sharing) {
        return $self->GetSharings()[sharing]->GetCircuitBuildRoutine();
    }

    uint32_t GetNumNonLinearOperations(e_sharing sharing) {
        return $self->GetSharings()[sharing]->GetNumNonLinearOperations();
    }
};

%rename(Circuit) Circuit;
%ignore Circuit::~Circuit;
%ignore Circuit::Init;
%ignore Circuit::Cleanup;
%ignore Circuit::Reset;

%ignore Circuit::GetLocalQueueOnLvl;
%ignore Circuit::GetInteractiveQueueOnLvl;
%ignore Circuit::PrintInteractiveQueues;
%ignore Circuit::GetNumLocalLayers;
%ignore Circuit::GetNumInteractiveLayers;

%ignore Circuit::GetInputGatesForParty;
%ignore Circuit::GetOutputGatesForParty;

%ignore Circuit::GetGateSpecificOutput;
%ignore Circuit::GetOutputGateValue;
%ignore Circuit::GetNumVals;

%ignore Circuit::PutCallbackGate;
%ignore Circuit::PutCondSwapGate;
%ignore Circuit::PutConstantGate;
%ignore Circuit::PutTruthTableGate;
%ignore Circuit::PutTruthTableMultiOutputGate;

// There are many functions for inputting values into ABY, but we are always
// inputting bits at the end of the day. To reduce confusion, only keep the
// largest input method (64 bit inputs).
%ignore Circuit::PutCONSGate;
%ignore Circuit::PutINGate;
%ignore Circuit::PutSharedINGate;
%ignore Circuit::PutAssertGate;
%rename("%(lowercamelcase)s") Circuit::PutCONSGate(UGATE_T, uint32_t);
%rename("%(lowercamelcase)s") Circuit::PutINGate(uint64_t, uint32_t, e_role);
%rename("%(lowercamelcase)s") Circuit::PutSharedINGate(uint64_t, uint32_t);
%rename("%(lowercamelcase)s") Circuit::PutAssertGate(uint64_t, uint32_t);

// Ignore SIMD stuff for now
%rename("$ignore", regextarget=1, fullname=1) "Circuit::.*SIMD";
%ignore Circuit::PutCombinerGate;
%ignore Circuit::PutSplitterGate;
%ignore Circuit::PutSubsetGate;
%ignore Circuit::PutCombineAtPosGate;
%ignore Circuit::PutPermutationGate;


// Additional Gates
%rename("%(lowercamelcase)s") PutInt32DIVGate;
%rename("%(lowercamelcase)s") PutMinGate;
%rename("%(lowercamelcase)s") PutMaxGate;


%rename(Share) share;
// %rename(ArithmeticShare) arithshare;
// %rename(BooleanShare) boolshare;
%ignore share::~share;
%ignore share::init;
%ignore share::get_clear_value_ptr;
%ignore share::get_clear_value_vec; // TODO: we need this for SIMD
%rename("%(lowercamelcase)s") create_new_share;

%rename(CircuitType) e_circuit;
%ignore C_LAST;
%rename(MultiplicationTripleGenerationAlgorithm) e_mt_gen_alg;
%rename(Phase) ABYPHASE;
%rename(Role) e_role;
%rename(SharingType) e_sharing;
%ignore S_LAST;

%rename(SecurityLevel) SECURITYLEVELS;
%rename("%s") ST;
%rename("%s") MT;
%rename("%s") LT;
%rename("%s") XLT;
%rename("%s") XXLT;

%{
#include "abycore/aby/abyparty.h"
#include "abycore/circuit/circuit.h"
#include "abycore/circuit/extra-gates.h"
#include "abycore/circuit/share.h"
#include "abycore/sharing/sharing.h"
%}

%include "abycore/aby/abyparty.h"
%include "abycore/ABY_utils/ABYconstants.h"
%include "abycore/circuit/circuit.h"
%include "abycore/circuit/extra-gates.h"
%include "abycore/circuit/share.h"
%include "ENCRYPTO_utils/constants.h"
%include "ENCRYPTO_utils/crypto/crypto.h"
%include "ENCRYPTO_utils/timer.h"
%include "ENCRYPTO_utils/typedefs.h"

// Expand templates.
//
// Note: these have to be placed after the imports unlike the renames, which
// have to be placed before. Also, we have to undo the renaming we did above,
// otherwise the %template directive runs into naming problems.
%rename(getClearValue8) share::get_clear_value<uint8_t>;
%rename(getClearValue16) share::get_clear_value<uint16_t>;
%rename(getClearValue32) share::get_clear_value<uint32_t>;
%rename(getClearValue64) share::get_clear_value<uint64_t>;
%template() share::get_clear_value<uint8_t>;
%template() share::get_clear_value<uint16_t>;
%template() share::get_clear_value<uint32_t>;
%template() share::get_clear_value<uint64_t>;
