var proofpointURL = SailPoint.CONTEXT_PATH + '/plugins/pluginPage.jsf?pn=ProofpointPlugin';
jQuery(document).ready(function(){
    jQuery("ul.navbar-right li:first")
        .before(
            '<li class="dropdown">' +
            '		<a href="' + proofpointURL + '" tabindex="0" role="menuitem" title="Proofpoint Plugin Settings Page">' +
            '			<i role="presenation" class="fa fa fa-envelope fa-lg"></i>' +
            '		</a>' +
            '</li>'
        );
});
