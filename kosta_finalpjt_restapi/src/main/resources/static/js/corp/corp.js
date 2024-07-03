// 부서 추가 폼 모달 열기
$(document).ready(function () {
    $('.btn_square').click(function () {
        // Show modal content
        $('#deptsModal .modal-body').html();
        $('#deptsModal .modal-footer').html();

        // Show the modal
        $('#deptsModal').modal('show');

        $('#deptsModalLabel').html('부서추가');

        // Change the style of the form
        $('#depteditform').hide();
        $('#deptform').show();

    });

    $('.btn-close').click(function () {
        // Clear previous modal content
        $('#deptsModal .modal-body').empty();
        $('#deptsModal .modal-footer').empty();

        // Change the style of the form
        $('#depteditform').show();
        $('#deptform').hide();

    });
});

// 부서 추가하기
const deptaddbtn = () => {
    let formData = $('#deptform').serialize(); // Serialize form data

    $.ajax({
        type: 'POST',
        url: '/admin/corp/deptadd', // URL to submit the form data
        data: formData,
        success: (response) => {
            alert('부서가 추가되었습니다!'); // Show success message
            // Optionally, you can handle UI updates or other actions on success
            $('#deptnmid').val("");
            $('#mgridid').val("");
            $(".btn-close").click(); // Close the modal
            window.location.href = '/corp/deptlist'; // Redirect to department list page
        },
        error: (xhr, status, error) => {
            alert('부서 추가 중 오류가 발생했습니다.'); // Show error message
            console.error(xhr); // Log the error for debugging
            // Optionally, you can handle specific errors or do additional error handling here
        }
    });
};

// 부서 수정하기
const deptedit = () => {
    let formData = $('#depteditform').serialize(); // Serialize form data

    $.ajax({
        type: 'POST',
        url: '/admin/corp/deptedit', // URL to submit the form data
        data: formData,
        success: (response) => {
            console.log(response)
            alert('부서가 수정되었습니다!'); // Show success message
            // Optionally, you can handle UI updates or other actions on success
            $(".btn-close").click(); // Close the modal
            window.location.href = '/corp/deptlist'; // Redirect to department list page
        },
        error: (xhr, status, error) => {
            alert('부서 수정 중 오류가 발생했습니다.'); // Show error message
            console.error(xhr); // Log the error for debugging
            // Optionally, you can handle specific errors or do additional error handling here
        }
    });
};

// 부서 수정 폼 모달 변경
function depteditbtn() {
    $('#deptsModalLabel').html('부서수정');
    // Toggle visibility of span and input elements for joblvidx
    $('#deptidSpan').toggle();
    $('#deptidInput').toggle();

    // Toggle visibility of span and input elements for joblvid
    $('#deptnmSpan').toggle();
    $('#deptnmInput').toggle();

    // Toggle visibility of span and input elements for joblvnm
    $('#mgridSpan').toggle();
    $('#mgridInput').toggle();

    $('#depteditbtnid').toggle();
    $('#depteditid').toggle();

    $('#memberlist').toggle();
}

// 부서 상세 모달
// $(document).ready(function () {
//     $('.dept-detail-link').click(function (e) {
//         e.preventDefault(); // Prevent default anchor behavior

//         var deptid = parseInt($(this).data('deptid'), 10); // Get department ID from data attribute
//         console.log(deptid);

//         $.ajax({
//             type: 'GET',
//             url: '/corp/deptinfo', // Endpoint to fetch department details
//             data: { deptid: deptid }, // Pass department ID as parameter
//             dataType: 'json',
//             success: function (obj) {
//                 $('#deptsModalLabel').html('부서상세');

//                 // Show the modal
//                 $('#deptsModal').modal('show');

//                 // Update form fields with received data
//                 $('#deptidSpan').text(obj.d.deptid);  // Update department ID span
//                 $('#deptidInput').val(obj.d.deptid);  // Update department ID input (if needed)
//                 $('#deptnmSpan').text(obj.d.deptnm);  // Update department name span
//                 $('#deptnmInput').val(obj.d.deptnm);  // Update department name input (if needed)
//                 $('#deptnmInput').val(obj.d.deptnm);  // Update department name input (if needed)

//                 // Update department manager span and input
//                 if (obj.d.mgrid && obj.d.mgrid.userid && obj.d.mgrid.userid.usernm) {
//                     $('#mgridSpan').text(obj.d.mgrid.userid.usernm);
//                     $('#mgridInput').val(obj.d.mgrid.userid.usernm);
//                 } else {
//                     $('#mgridSpan').text('');
//                     $('#mgridInput').val('');
//                 }

//                 // Update member list select options
//                 var memberlist = $('#memberlist');
//                 memberlist.empty(); // Clear existing options

//                 // Populate options from obj.mlist
//                 obj.mlist.forEach(function (m) {
//                     var option = $('<option>').text(m.userid?.usernm).attr('value', m.memberid);

//                     // Check if this option should be selected
//                     if (obj.d.mgrid?.userid?.usernm === m.userid?.usernm) {
//                         option.attr('selected', 'selected');
//                     }

//                     memberlist.append(option); // Append the option to select
//                 });

//                 // Add a default "Select" option if department manager is not defined
//                 if (!obj.d.mgrid?.userid?.usernm) {
//                     var blankOption = $('<option>').text('Select').attr('value', '');
//                     blankOption.attr('selected', 'selected');
//                     memberlist.prepend(blankOption); // Add blank option at the beginning
//                 }

//                 $('#deptdelid').attr('onclick', `location.href='/admin/corp/deptdel?deptid=${obj.d.deptid}'`);

//                 // Change the style of the forms
//                 $('#deptform').hide();
//                 $('#depteditform').show();
//             },
//             error: function (xhr, status, error) {
//                 console.error(xhr);
//                 alert('Failed to fetch department details.');
//             }
//         });
//     });
// });

$(document).ready(function () {
    $('.dept-detail-link').click(function (e) {
        e.preventDefault(); // Prevent default anchor behavior

        var deptid = parseInt($(this).data('deptid'), 10); // Get department ID from data attribute
        console.log(deptid);

        $.ajax({
            type: 'GET',
            url: '/corp/deptinfo', // Endpoint to fetch department details
            data: { deptid: deptid }, // Pass department ID as parameter
            dataType: 'json',
            success: function (obj) {
                $('#deptsModalLabel').html('부서상세');

                // Show the modal
                $('#deptsModal').modal('show');

                // Update form fields with received data
                $('#deptidSpan').text(obj.d.deptid);  // Update department ID span
                $('#deptidInput').val(obj.d.deptid);  // Update department ID input (if needed)
                $('#deptnmSpan').text(obj.d.deptnm);  // Update department name span
                $('#deptnmInput').val(obj.d.deptnm);  // Update department name input (if needed)

                // Update department manager span and input
                if (obj.d.mgrid && obj.d.mgrid.userid && obj.d.mgrid.userid.usernm) {
                    $('#mgridSpan').text(obj.d.mgrid.userid.usernm);
                    $('#mgridInput').val(obj.d.mgrid.userid.usernm);
                } else {
                    $('#mgridSpan').text('');
                    $('#mgridInput').val('');
                }

                // Update member list select options
                var memberlist = $('#memberlist');
                memberlist.empty(); // Clear existing options

                // Add default blank option
                var blankOption = $('<option>').text('선택 안함').attr('value', '');
                memberlist.append(blankOption);

                // Populate options from obj.mlist
                obj.mlist.forEach(function (m) {
                    var option = $('<option>').text(m.userid?.usernm).attr('value', m.memberid);

                    // Check if this option should be selected
                    if (obj.d.mgrid?.userid?.usernm === m.userid?.usernm) {
                        option.attr('selected', 'selected');
                    }

                    memberlist.append(option); // Append the option to select
                });

                $('#deptdelid').attr('onclick', `location.href='/admin/corp/deptdel?deptid=${obj.d.deptid}'`);

                // Change the style of the forms
                $('#deptform').hide();
                $('#depteditform').show();
            },
            error: function (xhr, status, error) {
                console.error(xhr);
                alert('Failed to fetch department details.');
            }
        });
    });
});


$(document).ready(function () {
    $('#deptdetailcalid').click(function () {
        $('#deptidSpan').show(); // Show deptidSpan
        $('#deptidInput').hide(); // Hide deptidInput

        $('#deptnmSpan').show(); // Show deptnmSpan
        $('#deptnmInput').hide(); // Hide deptnmInput

        $('#mgridSpan').show(); // Show mgridSpan
        $('#mgridInput').hide(); // Hide mgridInput

        $('#depteditbtnid').show(); // Show depteditbtnid
        $('#depteditid').hide(); // Hide depteditid

        $('#memberlist').hide(); // Hide memberlist
    });
});

// 직급 추가 폼 모달 열기
$(document).ready(function () {
    $('.btn_square').click(function () {
        // Clear previous modal content
        $('#joblvsModal .modal-body').empty();
        $('#joblvsModal .modal-footer').empty();

        // Construct the HTML for the form and footer buttons
        var formHtml = `
            <form id="joblvform">
                <table>
                    <tr>
                        <td class="form_td">직급번호</td>
                        <td class="form_td">
                            <input type="text" name="joblvid" id="joblvidid">
                        </td>
                    </tr>
                    <tr>
                        <td class="form_td">직급이름</td>
                        <td class="form_td">
                            <input type="text" name="joblvnm" id="joblvnmid">
                        </td>
                    </tr>
                </table>
                <div class="modal-footer">
                    <button type="button" class="btn blue_btn" onclick="joblvaddbtn()">직급추가</button>
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                </div>
            </form>
        `
            ;

        // Inject the form HTML into the modal body and footer
        $('#joblvsModal .modal-body').html(formHtml);

        // Show the modal
        $('#joblvsModal').modal('show');
    });
});

// 직급 추가하기
const joblvaddbtn = () => {
    let formData = $('#joblvform').serialize(); // Serialize form data

    $.ajax({
        type: 'POST',
        url: '/admin/corp/joblvadd', // URL to submit the form data
        data: formData,
        success: (response) => {
            alert('직급이 추가되었습니다!'); // Show success message
            // Optionally, you can handle UI updates or other actions on success
            $('#joblvidid').val("");
            $('#joblvnmid').val("");
            $(".btn-close").click(); // Close the modal
            window.location.href = '/corp/joblvlist'; // Redirect to department list page
        },
        error: (xhr, status, error) => {
            alert('직급 추가 중 오류가 발생했습니다.'); // Show error message
            console.error(xhr); // Log the error for debugging
            // Optionally, you can handle specific errors or do additional error handling here
        }
    });
};


// 직급 수정하기
const joblvedit = () => {
    let formData = $('#joblveditform').serialize(); // Serialize form data

    $.ajax({
        type: 'POST',
        url: '/admin/corp/joblvedit', // URL to submit the form data
        data: formData,
        success: (response) => {
            console.log(response)
            alert('직급이 수정되었습니다!'); // Show success message
            // Optionally, you can handle UI updates or other actions on success
            $(".btn-close").click(); // Close the modal
            window.location.href = '/corp/joblvlist'; // Redirect to department list page
        },
        error: (xhr, status, error) => {
            alert('직급 수정 중 오류가 발생했습니다.'); // Show error message
            console.error(xhr); // Log the error for debugging
            // Optionally, you can handle specific errors or do additional error handling here
        }
    });
};

// 직급 수정 폼 모달 변경
function joblveditbtn() {
    // Toggle visibility of span and input elements for joblvidx
    $('#joblvidxSpan').toggle();
    $('#joblvidxInput').toggle();

    // Toggle visibility of span and input elements for joblvid
    $('#joblvidSpan').toggle();
    $('#joblvidInput').toggle();

    // Toggle visibility of span and input elements for joblvnm
    $('#joblvnmSpan').toggle();
    $('#joblvnmInput').toggle();

    $('#joblveditbtnid').toggle();
    $('#joblveditid').toggle();
}

// 직급 상세 모달
$(document).ready(function () {
    $('.joblv-detail-link').click(function (e) {
        e.preventDefault(); // Prevent default anchor behavior
        // Clear previous modal content
        $('#joblvsModal .modal-body').empty();
        $('#joblvsModal .modal-footer').empty();

        var joblvidx = parseInt($(this).data('joblvidx'), 10); // Get joblvidx from data attribute

        $.ajax({
            type: 'GET',
            url: '/corp/joblvinfo', // Endpoint to fetch job level details
            data: { joblvidx: joblvidx }, // Pass joblvidx as parameter
            dataType: 'json',
            success: function (obj) {
                $('#joblvsModalLabel').html('직급상세');
                // Construct HTML for displaying job level details
                var formHtml = `
                    <form id="joblveditform">
                        <table>
                            <tr>
                                <td class="form_td">직급인덱스</td>
                                <td class="form_td"><span id="joblvidxSpan" style="display:'';">${obj.j.joblvidx}</span><input type="text" id="joblvidxInput" name="joblvidx" value="${obj.j.joblvidx}" style="display:none;" readonly></td>
                            </tr>
                            <tr>
                                <td class="form_td">직급번호</td>
                                <td class="form_td"><span id="joblvidSpan" style="display:'';">${obj.j.joblvid}</span><input type="text" id="joblvidInput" name="joblvid" value="${obj.j.joblvid}" style="display:none;"></td>
                            </tr>
                            <tr>
                                <td class="form_td">직급이름</td>
                                <td class="form_td"><span id="joblvnmSpan" style="display:'';">${obj.j.joblvnm}</span><input type="text" id="joblvnmInput" name="joblvnm" value="${obj.j.joblvnm}" style="display:none;"></td>
                            </tr>
                        </table>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" id="joblveditbtnid" onclick="joblveditbtn()" style="display:'';">직급수정</button>
                            <button type="button" class="btn btn-secondary" id="joblveditid" onclick="joblvedit()" style="display:none;">직급수정</button>
                            <button type="button" class="btn btn-secondary" id="joblvdelid" onclick="location.href='/admin/corp/joblvdel?joblvidx=${obj.j.joblvidx}'">직급삭제</button>
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                        </div>
                     </form>
                `;

                // Inject the HTML into the modal body
                $('#joblvsModal .modal-body').html(formHtml);

                // Show the modal
                $('#joblvsModal').modal('show');
            },
            error: function (xhr, status, error) {
                console.error(xhr);
                alert('Failed to fetch job level details.');
            }
        });
    });
});